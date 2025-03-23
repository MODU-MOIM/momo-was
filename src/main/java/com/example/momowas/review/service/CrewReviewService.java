package com.example.momowas.review.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.dto.CrewReviewReqDto;
import com.example.momowas.review.repository.CrewReviewRepository;
import com.example.momowas.schedule.domain.Schedule;
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

    /* 크루 평가 생성 */
    @Transactional
    public Long createCrewReview(CrewReviewReqDto crewReviewReqDto, Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Schedule schedule = scheduleService.getScheduleByScheduleId(crewReviewReqDto.scheduleId());

        CrewReview crewReview = crewReviewRepository.save(crewReviewReqDto.toEntity(crew, crewMember, schedule)); //크루 리뷰 저장

        List<Keyword> keywords = crewReviewReqDto.keywords();
        for (Keyword keyword : keywords) {
            crewReviewKeywordService.createCrewReviewKeyword(keyword, crewReview); //크루 리뷰-키워드 저장
        }

        return crewReview.getId();
    }
}
