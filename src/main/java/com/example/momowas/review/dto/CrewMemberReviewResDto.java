package com.example.momowas.review.dto;

import com.example.momowas.review.domain.CrewMemberReview;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CrewMemberReviewResDto {
    private Long reviewId;
    private String targetName;
    private String writerName;
    private String targetProfileImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String comment;
    private Double rating;

    @Builder
    public CrewMemberReviewResDto(Long reviewId, String targetName, String writerName, String targetProfileImage, LocalDateTime createdAt, LocalDateTime modifiedAt, String comment, Double rating) {
        this.reviewId = reviewId;
        this.targetName = targetName;
        this.writerName = writerName;
        this.targetProfileImage = targetProfileImage;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comment = comment;
        this.rating = rating;
    }

    public static CrewMemberReviewResDto fromEntity(CrewMemberReview crewMemberReview){
        return CrewMemberReviewResDto.builder()
                .reviewId(crewMemberReview.getId())
                .targetName(crewMemberReview.getTarget().getUser().getNickname())
                .writerName(crewMemberReview.getWriter().getUser().getNickname())
                .targetProfileImage(crewMemberReview.getTarget().getUser().getProfileImage())
                .createdAt(crewMemberReview.getCreatedAt())
                .modifiedAt(crewMemberReview.getModifiedAt())
                .comment(crewMemberReview.getComment())
                .rating(crewMemberReview.getRating())
                .build();
    }
}
