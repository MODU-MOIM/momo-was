package com.example.momowas.record.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.record.domain.Archive;
import com.example.momowas.record.dto.ArchiveReqDto;
import com.example.momowas.record.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;

    /* 기록 생성 */
    public Long createArchive(ArchiveReqDto archiveReqDto, Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        Archive archive = archiveRepository.save(archiveReqDto.toEntity(crew, crewMember));
        return archive.getId();
    }
}
