package com.example.momowas.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/* 다대다 매핑 테이블 - 크루 평가, 키워드 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CrewReviewKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_review_id")
    private CrewReview crewReview;

    @Enumerated(EnumType.STRING)
    private Keyword keyword;

    @Builder
    private CrewReviewKeyword(Keyword keyword, CrewReview crewReview) {
        this.keyword = Objects.requireNonNull(keyword, "keyword는 null이 될 수 없습니다.");
        this.crewReview = Objects.requireNonNull(crewReview, "crewReview는 null이 될 수 없습니다.");
    }
}
