package com.example.momowas.assessment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

/* 다대다 매핑 테이블 - 크루 평가, 키워드 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewAssessmentKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_assessment_id")
    private CrewAssessment crewAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @Builder
    private CrewAssessmentKeyword(CrewAssessment crewAssessment, Keyword keyword) {
        this.keyword = Objects.requireNonNull(keyword, "keyword는 null이 될 수 없습니다.");
        this.crewAssessment = Objects.requireNonNull(crewAssessment, "crewAssessment는 null이 될 수 없습니다.");
    }
}
