package com.example.momowas.review.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CrewReviewTotalInfoResDto(
        Double averageRatings,
        List<CrewReviewKeywordCountListResDto> keywordCount,
        List<CrewReviewListResDto> crewReviewList
) {
    public static CrewReviewTotalInfoResDto of(Double averageRatings, List<CrewReviewKeywordCountListResDto> keywordCount, List<CrewReviewListResDto> crewReviewList) {
        return CrewReviewTotalInfoResDto.builder()
                .averageRatings(averageRatings)
                .keywordCount(keywordCount)
                .crewReviewList(crewReviewList)
                .build();
    }
}
