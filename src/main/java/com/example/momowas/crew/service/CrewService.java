package com.example.momowas.crew.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.domain.CrewDocument;
import com.example.momowas.crew.dto.*;
import com.example.momowas.crew.repository.CrewElasticRepository;
import com.example.momowas.crew.repository.CrewRepository;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.dto.CrewMemberListResDto;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.crewregion.service.CrewRegionService;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService {
    private final CrewRepository crewRepository;
    private final CrewMemberService crewMemberService;
    private final CrewRegionService crewRegionService;
    private final UserService userService;
    private final S3Service s3Service;
    private final RecommendService recommendService;
    private final CrewElasticRepository crewElasticRepository;

    /* 크루 id로 크루 조회 */
    @Transactional(readOnly = true)
    public Crew findCrewById(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_CREW));
    }

    /* 크루 생성 */
    @Transactional
    public Long createCrew(CrewReqDto crewReqDto, MultipartFile file, Long userId) throws IOException {
        validateCrewName(crewReqDto.name());

        User user = userService.findUserById(userId);
        String bannerImageUrl = s3Service.uploadImage(file, "crew");

        Crew crew = crewRepository.save(crewReqDto.toEntity(bannerImageUrl)); //크루 저장
        this.indexCrew(crew); //인덱스에 크루 저장
        crewRegionService.createCrewRegion(crewReqDto.regions(), crew); //크루-지역 저장
        crewMemberService.createLeader(user, crew); //크루 멤버 저장

        recommendService.handleCrewEvent(crew.getId(), "createCrew", 0, crew.getMaxMembers());

        return crew.getId();
    }

    /* 전체 크루 조회 */
    @Transactional(readOnly = true)
    public List<CrewListResDto> getCrewList() {
        return crewRepository.findAll().stream().map(crew -> {
            List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId()); //크루 id로 지역 찾기
            Integer memberCount=crewMemberService.getCrewMemberList(crew.getId()).size(); //크루 멤버 수
            return CrewListResDto.of(crew,regionDtos,memberCount);
        }).collect(Collectors.toList());
    }

    /* 특정 크루 조회 */
    @Transactional(readOnly = true)
    public CrewDetailResDto getCrewDetail(Long crewId) {
        Crew crew = findCrewById(crewId);
        List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId()); //크루 id로 지역 찾기
        Integer memberCount=crewMemberService.getCrewMemberList(crew.getId()).size(); //크루 멤버 수
        return CrewDetailResDto.of(crew,regionDtos,memberCount);
    }

    /* 특정 크루 삭제 */
    @Transactional
    public void deleteCrew(Long crewId) {
        Crew crew = findCrewById(crewId);

        crewRepository.delete(crew);
        crewElasticRepository.deleteByCrewId(crewId);
    }

    /* 크루명 및 배너사진 수정 */
    @Transactional
    public void updateCrewBasic(CrewNameReqDto crewNameReqDto, MultipartFile file, Long crewId) throws IOException {
        Crew crew = findCrewById(crewId);
        validateCrewName(crewNameReqDto.name());
        String bannerImageUrl= s3Service.uploadImage(file, "crew");

        crew.updateName(crewNameReqDto.name());
        crew.updateBannerImage(bannerImageUrl);

        this.indexCrew(crew); //인덱스 크루 명 수정
    }

    /* 크루 소개 수정 */
    @Transactional
    public void updateCrewReport(CrewReportReqDto crewReportReqDto, Long crewId){
        Crew crew = findCrewById(crewId);

        crewRegionService.updateCrewRegion(crew.getCrewRegions(),crewReportReqDto.regions(), crew);
        crew.updateCategory(crewReportReqDto.category());
        crew.updateDescription(crewReportReqDto.description());
    }

    /* 인원수 수정 */
    @Transactional
    public void updateCrewHeadCount(CrewHeadcountReqDto crewHeadcountReqDto, Long crewId) {
        Crew crew = findCrewById(crewId);

        crew.updateMinMembers(crewHeadcountReqDto.minMembers());
        crew.updateMaxMembers(crewHeadcountReqDto.maxMembers());
    }

    /* 가입 조건 수정 */
    @Transactional
    public void updateCrewCondition(CrewConditionReqDto crewConditionReqDto, Long crewId) {
        Crew crew = findCrewById(crewId);

        crew.updateMinAge(crewConditionReqDto.minAge());
        crew.updateMaxAge(crewConditionReqDto.maxAge());
        crew.updateGenderRestriction(crewConditionReqDto.genderRestriction());
    }

    /* 크루명 중복 검증 */
    @Transactional(readOnly = true)
    public void validateCrewName(String crewName) {
        if (crewRepository.existsByName(crewName)) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS_CREW);
        }
    }

    public List<CrewDetailResDto> search(Specification<Crew> spec){
        List<Crew> resultCrews = crewRepository.findAll(spec);

        return resultCrews.stream()
                .map(crew -> {
                    List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId());
                    Integer memberCount=crewMemberService.getCrewMemberList(crew.getId()).size(); //크루 멤버 수
                    return CrewDetailResDto.of(crew, regionDtos,memberCount);
                })
                .collect(Collectors.toList());
    }

    public List<Long> searchName(String name) {
        SearchHits<CrewDocument> searchHits = crewElasticRepository.findByName(name);

        List<Long> crewIdList = new ArrayList<>();

        for (SearchHit<CrewDocument> hit : searchHits) {
            CrewDocument crewDocument = hit.getContent();
            Long crewId = crewDocument.getCrewId();
            crewIdList.add(crewId);
        }
        return crewIdList;
    }

    private void indexCrew(Crew crew) {
        CrewDocument crewDocument = CrewDocument.builder()
                .crewId(crew.getId())
                .name(crew.getName())
                .build();
        crewElasticRepository.save(crewDocument);
    }

    public void indexAll() {
        List<Crew> crews = crewRepository.findAll();

        for (Crew crew : crews) {
            CrewDocument crewDocument = CrewDocument.builder()
                    .crewId(crew.getId())
                    .name(crew.getName())
                    .build();

            crewElasticRepository.save(crewDocument);
        }
    }
    public List<CrewListResDto> getCrewsByMe(Long userId){
        return crewRepository.findByCrewMembersUserId(userId).stream().map(crew -> {
            List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId());
            Integer memberCount=crewMemberService.getCrewMemberList(crew.getId()).size(); //크루 멤버 수
            return CrewListResDto.of(crew,regionDtos, memberCount);
        }).collect(Collectors.toList());
    }

}
