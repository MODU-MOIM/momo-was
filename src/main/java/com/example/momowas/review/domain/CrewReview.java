package com.example.momowas.review.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("CREW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CrewReview extends Review {

    @OneToMany(mappedBy = "crewReview", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewReviewKeyword> crewReviewKeywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_member_id")
    private CrewMember crewMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="schedule_id")
    private Schedule schedule;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    private CrewReview(String comment,
                       Double rating,
                       Crew crew,
                       CrewMember crewMember,
                       Schedule schedule) {
        if (!StringUtils.hasText(comment)) {
            throw new IllegalArgumentException("comment는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.comment = comment;
        this.rating = Objects.requireNonNull(rating, "rating은 null이 될 수 없습니다.");
        this.crew = Objects.requireNonNull(crew, "crew는 null이 될 수 없습니다.");
        this.crewMember = Objects.requireNonNull(crewMember, "crewMember는 null이 될 수 없습니다.");
        this.schedule = Objects.requireNonNull(schedule, "schedule는 null이 될 수 없습니다.");
    }

    /* 작성자인지 검증 */
    public boolean isWriter(CrewMember crewMember) {
        return this.crewMember.getId()==crewMember.getId();
    }

    /* 리뷰 수정 */
    public void updateComment(String comment) {
        if (!StringUtils.hasText(comment)) {
            throw new IllegalArgumentException("comment는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.comment=comment;
    }

    /* 별점 수정 */
    public void updateRating(Double rating) {
        this.rating = Objects.requireNonNull(rating, "rating은 null이 될 수 없습니다.");
    }
}
