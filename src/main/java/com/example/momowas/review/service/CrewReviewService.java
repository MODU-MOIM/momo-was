package com.example.momowas.review.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.dto.CrewReviewDetailResDto;
import com.example.momowas.review.dto.CrewReviewListResDto;
import com.example.momowas.review.dto.CrewReviewReqDto;
import com.example.momowas.review.repository.CrewReviewRepository;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.dto.ScheduleInfoResDto;
import com.example.momowas.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewReviewService {
    private final CrewReviewRepository crewReviewRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final ScheduleService scheduleService;
    private final CrewReviewKeywordService crewReviewKeywordService;

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

        crewReviewKeywordService.createCrewReviewKeyword(crewReviewReqDto.keywords(),crewReview); //크루 리뷰-키워드 저장

        return crewReview.getId();
    }

    /* 전체 크루 평가 조회 */
    @Transactional(readOnly = true)
    public List<CrewReviewListResDto> getCrewReviewList(Long crewId) {
        List<CrewReview> crewReviews = findCrewReviewByCrewId(crewId);

        List<CrewReviewListResDto> crewReviewListResDtos = crewReviews.stream()
                .map((crewReview) -> {
                    List<Keyword> keywords = crewReviewKeywordService.extractKeywordList(crewReview.getCrewReviewKeywords());
                    ScheduleInfoResDto scheduleInfoResDto = ScheduleInfoResDto.fromEntity(crewReview.getSchedule());
                    return CrewReviewListResDto.of(crewReview, keywords, scheduleInfoResDto);
                }).toList();

        return crewReviewListResDtos;
    }

    /* 특정 크루 평가 조회 */
    @Transactional(readOnly = true)
    public CrewReviewDetailResDto getCrewReviewDetail(Long reviewId){
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
        crewReviewKeywordService.updateCrewReviewKeyword(crewReviewReqDto.keywords(),crewReview);
    }

    /* 크루 평가 삭제 */
    @Transactional
    public void deleteCrewReview(Long reviewId, Long crewId, Long userId) {
        CrewReview crewReview = findCrewReviewById(reviewId);
        validateWriter(crewId, userId, crewReview);

        crewReviewRepository.deleteById(crewReview.getId());
    }

    /* 사용자가 평가 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, CrewReview crewReview) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!crewReview.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
