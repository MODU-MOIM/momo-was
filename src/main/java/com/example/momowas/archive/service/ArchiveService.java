package com.example.momowas.archive.service;

import com.example.momowas.archive.dto.ArchiveDetailResDto;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.archive.domain.Archive;
import com.example.momowas.archive.dto.ArchiveListResDto;
import com.example.momowas.archive.dto.ArchiveReqDto;
import com.example.momowas.archive.repository.ArchiveRepository;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.like.repository.LikeRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final LikeRepository likeRepository;

    /* 기록 id로 기록 조회 */
    @Transactional
    public Archive findArchiveById(Long archiveId) {
        return archiveRepository.findById(archiveId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ARCHIVE));
    }

    /* 기록 생성 */
    @Transactional
    public Long createArchive(ArchiveReqDto archiveReqDto, Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        Archive archive = archiveRepository.save(archiveReqDto.toEntity(crew, crewMember));
        return archive.getId();
    }

    /* 전체 기록 조회 */
    @Transactional(readOnly = true)
    public List<ArchiveListResDto> getArchiveList() {
        return archiveRepository.findAll().stream()
                .map(ArchiveListResDto::of)
                .toList();
    }

    /* 특정 피드 조회 */
    @Transactional(readOnly = true)
    public ArchiveDetailResDto getArchiveDetail(Long crewId, Long archiveId,Long userId) {
        Archive archive = findArchiveById(archiveId);
        User writer = archive.getCrewMember().getUser();
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        boolean isLiked = likeRepository.existsByArchiveAndCrewMember(archive, crewMember);
        return ArchiveDetailResDto.of(archive, writer,isLiked);

    }

    /* 기록 수정 */
    @Transactional
    public void updateArchive(ArchiveReqDto archiveReqDto, Long crewId, Long archiveId, Long userId){
        Archive archive = findArchiveById(archiveId);
        validateWriter(crewId, userId, archive);
        archive.update(archiveReqDto.title(), archiveReqDto.content(), archiveReqDto.thumbnailImageUrl());
    }

    /* 기록 삭제 */
    @Transactional
    public void deleteArchive(Long crewId, Long archiveId, Long userId) {
        Archive archive = findArchiveById(archiveId);
        validateWriter(crewId, userId, archive);
        archiveRepository.delete(archive);
    }

    /* 사용자가 기록 작성자인지 검증 */
    public void validateWriter(Long crewId, Long userId, Archive archive) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!archive.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
