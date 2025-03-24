package com.example.momowas.review.dto;

import com.example.momowas.review.domain.Keyword;
import lombok.Builder;

@Builder
public record CrewReviewKeywordCountListResDto(
        Keyword keyword,
        Integer count
) {
    public static CrewReviewKeywordCountListResDto of(Keyword keyword, Integer count) {
        return CrewReviewKeywordCountListResDto.builder()
                .keyword(keyword)
                .count(count)
                .build();
    }
}
