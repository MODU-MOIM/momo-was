package com.example.momowas.notice.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.notice.dto.NoticeReqDto;
import com.example.momowas.notice.repository.NoticeRepository;
import com.example.momowas.vote.domain.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;

    /* 공지 생성 */
    @Transactional
    public Notice createNotice(NoticeReqDto noticeReqDto, Long crewId, Long userId, Vote vote) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        return noticeRepository.save(noticeReqDto.toEntity(crew, crewMember,vote));
    }
}
