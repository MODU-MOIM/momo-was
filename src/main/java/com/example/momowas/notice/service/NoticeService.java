package com.example.momowas.notice.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.notice.dto.NoticeDetailResDto;
import com.example.momowas.notice.dto.NoticeListResDto;
import com.example.momowas.notice.dto.NoticeReqDto;
import com.example.momowas.notice.repository.NoticeRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteResDto;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.example.momowas.voteparticipant.service.VoteParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final VoteParticipantService voteParticipantService;

    /* 공지id로 공지 조회 */
    @Transactional(readOnly = true)
    public Notice findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_NOTICE));
    }

    /* 공지 생성 */
    @Transactional
    public Notice createNotice(NoticeReqDto noticeReqDto, Long crewId, Long userId, Vote vote) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        return noticeRepository.save(noticeReqDto.toEntity(crew, crewMember, vote));
    }

    /* 전체 공지 조회 */
    @Transactional(readOnly = true)
    public List<NoticeListResDto> getNoticeList(Long crewId) {
        Crew crew = crewService.findCrewById(crewId);

        List<NoticeListResDto> noticeListResDtos = crew.getNotices().stream().map((notice) -> {
            CrewMember crewMember = notice.getCrewMember();
            User user = crewMember.getUser();
            return NoticeListResDto.of(user, crewMember, notice);
        }).collect(Collectors.toList());

        return noticeListResDtos;
    }

    /* 특정 공지 조회 */
    @Transactional(readOnly = true)
    public NoticeDetailResDto getNoticeDetail(Long crewId, Long noticeId, Long userId) {
        Notice notice = findNoticeById(noticeId);

        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        VoteResDto voteResDto = voteParticipantService.getVoteDetail(notice.getVote(), crewMember);

        CrewMember writer = notice.getCrewMember();
        return NoticeDetailResDto.of(writer.getUser(), writer, notice, voteResDto);
    }
}
