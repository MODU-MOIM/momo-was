package com.example.momowas.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CrewMemberReviewReqDto {
    private String comment;
    private Double rating;

    @Builder
    public CrewMemberReviewReqDto(String comment, Double rating) {
        this.comment = comment;
        this.rating = rating;
    }
}
