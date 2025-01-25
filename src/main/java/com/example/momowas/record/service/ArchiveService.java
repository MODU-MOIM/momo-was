package com.example.momowas.record.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.record.domain.Archive;
import com.example.momowas.record.dto.ArchiveListResDto;
import com.example.momowas.record.dto.ArchiveReqDto;
import com.example.momowas.record.repository.ArchiveRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
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

    /* 기록 수정 */
    @Transactional
    public void updateArchive(ArchiveReqDto archiveReqDto, Long crewId, Long archiveId, Long userId){
        Archive archive = findArchiveById(archiveId);
        validateWriter(crewId, userId, archive);
        archive.update(archiveReqDto.title(), archiveReqDto.content(), archiveReqDto.thumbnailImageUrl());
    }

    /* 사용자가 기록 작성자인지 검증 */
    public void validateWriter(Long crewId, Long userId, Archive archive) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!archive.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
