package com.example.momowas.review.dto;

import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.schedule.dto.ScheduleInfoResDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CrewReviewDetailResDto(
        Long reviewId,
        String writer,
        String profileImage,
        LocalDateTime createdAt,
        ScheduleInfoResDto scheduleInfo,
        List<Keyword> keywords,
        String comment,
        Double rating
) {
    public static CrewReviewDetailResDto of(CrewReview crewReview, List<Keyword> keywords, ScheduleInfoResDto scheduleInfoResDto) {
        return CrewReviewDetailResDto.builder()
                .reviewId(crewReview.getId())
                .writer(crewReview.getCrewMember().getUser().getNickname())
                .profileImage(crewReview.getCrewMember().getUser().getProfileImage())
                .createdAt(crewReview.getCreatedAt())
                .scheduleInfo(scheduleInfoResDto)
                .keywords(keywords)
                .comment(crewReview.getComment())
                .rating(crewReview.getRating())
                .build();
    }
}
