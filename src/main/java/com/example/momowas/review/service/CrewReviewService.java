package com.example.momowas.review.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.dto.*;
import com.example.momowas.review.repository.CrewReviewRepository;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.dto.ScheduleInfoResDto;
import com.example.momowas.schedule.service.ScheduleService;
import com.example.momowas.sse.repository.SseEmitterRepository;
import com.example.momowas.sse.service.SseEmitterService;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewReviewService {
    private final CrewReviewRepository crewReviewRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final ScheduleService scheduleService;
    private final CrewReviewKeywordService crewReviewKeywordService;
    private final SseEmitterService sseEmitterService;
    private final SseEmitterRepository sseEmitterRepository;


    /* 평가 id로 크루 평가 조회 */
    @Transactional(readOnly = true)
    public CrewReview findCrewReviewById(Long crewReviewId) {
        return crewReviewRepository.findById(crewReviewId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_REVIEW));
    }

    /* 크루 id로 크루 평가 조회 */
    @Transactional(readOnly = true)
    public List<CrewReview> findCrewReviewByCrewId(Long crewId) {
        return crewReviewRepository.findByCrewId(crewId);
    }

    /* 크루 평가 생성 */
    @Transactional
    public Long createCrewReview(CrewReviewReqDto crewReviewReqDto, Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Schedule schedule = scheduleService.getScheduleByScheduleId(crewReviewReqDto.scheduleId());

        CrewReview crewReview = crewReviewRepository.save(crewReviewReqDto.toEntity(crew, crewMember, schedule)); //크루 리뷰 저장

        crewReviewKeywordService.createCrewReviewKeyword(crewReviewReqDto.keywords(), crewReview); //크루 리뷰-키워드 저장

        return crewReview.getId();
    }

    /* 전체 크루 평가 조회 */
    @Transactional(readOnly = true)
    public CrewReviewTotalInfoResDto getCrewReviewList(Long crewId) {
        List<CrewReview> crewReviews = findCrewReviewByCrewId(crewId);

        //전체 평가 리스트
        List<CrewReviewListResDto> crewReviewListResDtos = crewReviews.stream()
                .map((crewReview) -> {
                    List<Keyword> keywords = crewReviewKeywordService.extractKeywordList(crewReview.getCrewReviewKeywords());
                    ScheduleInfoResDto scheduleInfoResDto = ScheduleInfoResDto.fromEntity(crewReview.getSchedule());
                    return CrewReviewListResDto.of(crewReview, keywords, scheduleInfoResDto);
                }).toList();

        //매너 점수
        Crew crew = crewService.findCrewById(crewId);
        double mannersRating = crew.countMannersRating();

        //키워드별 개수
        List<CrewReviewKeywordCountListResDto> crewReviewKeywordCountListResDtos = crewReviewKeywordService.countCrewReviewKeywords(crewId);

        return CrewReviewTotalInfoResDto.of(mannersRating, crewReviewKeywordCountListResDtos, crewReviewListResDtos);
    }

    /* 특정 크루 평가 조회 */
    @Transactional(readOnly = true)
    public CrewReviewDetailResDto getCrewReviewDetail(Long reviewId) {
        CrewReview crewReview = findCrewReviewById(reviewId);

        List<Keyword> keywords = crewReviewKeywordService.extractKeywordList(crewReview.getCrewReviewKeywords());
        ScheduleInfoResDto scheduleInfoResDto = ScheduleInfoResDto.fromEntity(crewReview.getSchedule());
        return CrewReviewDetailResDto.of(crewReview, keywords, scheduleInfoResDto);
    }

    /* 크루 평가 수정 */
    @Transactional
    public void updateCrewReview(CrewReviewReqDto crewReviewReqDto, Long reviewId, Long crewId, Long userId) {
        CrewReview crewReview = findCrewReviewById(reviewId);
        validateWriter(crewId, userId, crewReview);

        crewReview.updateComment(crewReviewReqDto.comment());
        crewReview.updateRating(crewReviewReqDto.rating());
        crewReviewKeywordService.updateCrewReviewKeyword(crewReviewReqDto.keywords(), crewReview);
    }

    /* 크루 평가 삭제 */
    @Transactional
    public void deleteCrewReview(Long reviewId, Long crewId, Long userId) {
        CrewReview crewReview = findCrewReviewById(reviewId);
        validateWriter(crewId, userId, crewReview);

        crewReviewRepository.deleteById(crewReview.getId());
    }

    /* 모임 일정 다음 날 자정에 크루 리뷰 요청 알림 전송 */
    @Transactional
    //@Scheduled(cron = "0 0 0 1/1 * ?")  //매일 자정마다 실행
    @Scheduled(cron = "0 0/1 * 1/1 * ?") //1분 주기(테스트)
    public void sendCrewReviewNotification() {
        LocalDate previousDay = LocalDate.now().minusDays(1);
        List<Schedule> schedules = scheduleService.getSchedulesByDate(previousDay);

        schedules.stream()
                .forEach((schedule) -> {

                    //테스트용
                    for (Long userId : sseEmitterRepository.getAllUserIds()) {
                        sseEmitterService.sendToClient("test", userId, "ping");
                    }
                    //

                    Vote vote = schedule.getNotice().getVote();

                    List<VoteParticipant> positiveParticipants = vote.getVoteParticipants().stream()
                            .filter((voteParticipant) ->
                                    voteParticipant.getStatus() == VoteStatus.POSITIVE
                            ).toList(); //일정 공지 투표에 '참석'에 투표한 참여자들

                    // 알림 전송
                    Crew crew = crewService.findCrewById(schedule.getCrewId());

                    ScheduleReviewEventResDto payload = ScheduleReviewEventResDto.of(crew, schedule);

                    for (VoteParticipant participant : positiveParticipants) {
                        Long userId = participant.getCrewMember().getUser().getId();
                        //sseEmitterService.broadcast("review", userId, payload);
                        sseEmitterService.sendToClient("review", userId, "test");
                    }
                });
    }

    @Scheduled(cron = "0/30 * * * * ?") // 30초마다 실행
    public void sendHeartBeatMessage() {
        for (Long userId : sseEmitterRepository.getAllUserIds()) {
            sseEmitterService.sendToClient("heartbeat", userId, "ping");
        }
    }


    /* 사용자가 평가 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, CrewReview crewReview) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if (!crewReview.isWriter(crewMember)) {
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
