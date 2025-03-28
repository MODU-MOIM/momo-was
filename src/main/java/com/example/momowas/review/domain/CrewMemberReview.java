package com.example.momowas.review.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CREW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CrewMemberReview extends Review{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private CrewMember writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private CrewMember target;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public CrewMemberReview(CrewMember writer, CrewMember target, String comment, Double rating, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(comment, rating);
        this.writer = writer;
        this.target = target;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
