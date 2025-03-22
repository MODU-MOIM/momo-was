package com.example.momowas.notice.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.notice.domain.NoticeType;
import com.example.momowas.notice.dto.NoticeDetailResDto;
import com.example.momowas.notice.dto.NoticeListResDto;
import com.example.momowas.notice.dto.NoticeReqDto;
import com.example.momowas.notice.repository.NoticeRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.user.domain.User;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.domain.VoteType;
import com.example.momowas.vote.dto.VoteListResDto;
import com.example.momowas.vote.dto.VoteReqDto;
import com.example.momowas.vote.dto.VoteDetailResDto;
import com.example.momowas.vote.service.VoteService;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import com.example.momowas.voteparticipant.service.VoteParticipantService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
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
    private final VoteService voteService;

    /* 공지 id로 공지 조회 */
    @Transactional(readOnly = true)
    public Notice findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_NOTICE));
    }

    /* 공지 생성 */
    @Transactional
    private Long createNotice(NoticeReqDto noticeReqDto, NoticeType noticeType, Long crewId, Long userId, Schedule schedule) {
        Vote vote = null;

        if (noticeReqDto.vote().isEnabled()) {
            vote = voteService.createVote(noticeReqDto.vote());
        }

        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        Notice notice=null;

        switch (noticeType){
            case GENERAL :
                notice = noticeReqDto.toEntity(crew, crewMember, vote, noticeType, null);
                break;
            case SCHEDULE :
                notice = noticeReqDto.toEntity(crew, crewMember, vote, noticeType, schedule);
                break;
        }

        return noticeRepository.save(notice).getId();
    }

    /* 일반 공지 생성 */
    @Transactional
    public Long createGeneralNotice(NoticeReqDto noticeReqDto, Long crewId, Long userId) {
        return createNotice(noticeReqDto, NoticeType.GENERAL, crewId, userId,null);
    }

    /* 일정 공지 생성 */
    @Transactional
    public Long createScheduleNotice(Schedule schedule, Long crewId, Long userId) {
        VoteReqDto voteReqDto = VoteReqDto.of(true, VoteType.ATTENDANCE, "정모 참여 투표 여부");
        NoticeReqDto noticeReqDto = NoticeReqDto.of(null, voteReqDto);

        return createNotice(noticeReqDto, NoticeType.SCHEDULE, crewId, userId, schedule);
    }

    /* 전체 공지 조회 */
    @Transactional(readOnly = true)
    public List<NoticeListResDto> getNoticeList(Long crewId) {
        Crew crew = crewService.findCrewById(crewId);

        List<NoticeListResDto> noticeListResDtos = crew.getNotices().stream().map((notice) -> {
            CrewMember crewMember = notice.getCrewMember();
            User user = crewMember.getUser();
            VoteListResDto voteListResDto = VoteListResDto.of(notice.getVote());

            return NoticeListResDto.of(user, crewMember, notice, voteListResDto);
        }).collect(Collectors.toList());

        return noticeListResDtos;
    }

    /* 특정 공지 조회 */
    @Transactional(readOnly = true)
    public NoticeDetailResDto getNoticeDetail(Long crewId, Long noticeId, Long userId) {
        Notice notice = findNoticeById(noticeId);
        Vote vote = notice.getVote();
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        VoteDetailResDto voteDetailResDto = null;

        if (vote == null) {
            voteDetailResDto = VoteDetailResDto.of();
        } else {
            VoteStatus voteStatus = voteParticipantService.getVoteStatus(vote, crewMember);
            voteDetailResDto = voteService.getVoteDetail(vote, voteStatus);
        }

        CrewMember writer = notice.getCrewMember();
        return NoticeDetailResDto.of(writer.getUser(), writer, notice, voteDetailResDto);
    }

    /* 공지 수정 */
    @Transactional()
    public void updateNotice(NoticeReqDto noticeReqDto, Long crewId, Long noticeId, Long userId) {
        Notice notice = findNoticeById(noticeId);
        notice.updateContent(noticeReqDto.content());

        updateNoticeVote(noticeReqDto, notice);
    }

    @Transactional
    private void updateNoticeVote(NoticeReqDto noticeReqDto, Notice notice) {
        VoteReqDto voteReqDto = noticeReqDto.vote();

        if (voteReqDto.isEnabled()) { //투표 활성화
            if (notice.getVote() != null) { //기존에 투표 존재 -> 수정
                voteService.updateVote(notice.getVote(), voteReqDto);
            } else { //기존에 투표 존재x -> 생성
                Vote vote = voteService.createVote(voteReqDto);
                notice.updateVote(vote);
            }
        } else { //투표 활성화 x
            if (notice.getVote() != null) { //기존에 투표 존재 -> 삭제
                Vote vote = notice.getVote();
                notice.deleteVote();
                voteService.deleteVote(vote);
            }
        }
    }

    /* 공지 삭제 */
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        noticeRepository.delete(notice);
    }

    /* 공지 상단 고정 또는 고정 되있으면 해제 */
    @Transactional
    public Long togglePinNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        notice.togglePinned();
        return noticeId;
    }
}
