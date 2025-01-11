package com.example.momowas.crew.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.dto.CreateCrewReqDto;
import com.example.momowas.crew.dto.CrewDetailResDto;
import com.example.momowas.crew.dto.CrewListResDto;
import com.example.momowas.crew.repository.CrewRepository;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.crewregion.service.CrewRegionService;
import com.example.momowas.region.domain.Region;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final CrewRepository crewRepository;
    private final CrewMemberService crewMemberService;
    private final CrewRegionService crewRegionService;
    private final UserService userService;

    /* 크루 생성 */
    @Transactional
    public Long createCrew(CreateCrewReqDto createCrewReqDto, Long userId) {

        validateCrewName(createCrewReqDto.name());

        Crew crew = crewRepository.save(createCrewReqDto.toEntity()); //크루 저장
        crewRegionService.createCrewRegion(createCrewReqDto.regions(), crew); //크루-지역 저장

        User user = userService.readById(userId);
        crewMemberService.createLeader(user, crew); //크루 멤버 저장

        return crew.getId();
    }

    /* 전체 크루 조회 */
    @Transactional(readOnly = true)
    public List<CrewListResDto> getCrewList() {
        return crewRepository.findAll().stream().map(crew -> {
            List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId()); //크루 id로 지역 찾기
            return CrewListResDto.of(crew,regionDtos);
        }).collect(Collectors.toList());

    }

    /* 특정 크루 조회 */
    @Transactional(readOnly = true)
    public CrewDetailResDto getCrewDetail(Long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_CREW));
        List<RegionDto> regionDtos = crewRegionService.findRegionByCrewId(crew.getId()); //크루 id로 지역 찾기
        return CrewDetailResDto.of(crew,regionDtos);
    }

    /* 특정 크루 삭제 */
    @Transactional
    public void deleteCrew(Long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_CREW));
        crewRepository.delete(crew);
    }

    /* 크루명 중복 검증 */
    @Transactional(readOnly = true)
    private void validateCrewName(String crewName) {
        if (crewRepository.existsByName(crewName)) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS_CREW);
        }
    }
}
