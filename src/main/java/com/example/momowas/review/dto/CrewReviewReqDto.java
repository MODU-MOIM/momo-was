package com.example.momowas.review.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.schedule.domain.Schedule;

import java.util.List;

public record CrewReviewReqDto(String comment,
                               Double rating,
                               List<Keyword> keywords,
                               Long scheduleId) {
    public CrewReview toEntity(Crew crew, CrewMember crewMember, Schedule schedule) {
        return CrewReview.builder()
                .comment(comment)
                .rating(rating)
                .crew(crew)
                .crewMember(crewMember)
                .schedule(schedule)
                .build();
    }
}
