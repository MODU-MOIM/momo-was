package com.example.momowas.review.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CrewReviewTotalInfoResDto(
        Double mannersRating,
        List<CrewReviewKeywordCountListResDto> keywordCount,
        List<CrewReviewListResDto> crewReviewList
) {
    public static CrewReviewTotalInfoResDto of(Double mannersRating, List<CrewReviewKeywordCountListResDto> keywordCount, List<CrewReviewListResDto> crewReviewList) {
        return CrewReviewTotalInfoResDto.builder()
                .mannersRating(mannersRating)
                .keywordCount(keywordCount)
                .crewReviewList(crewReviewList)
                .build();
    }
}
