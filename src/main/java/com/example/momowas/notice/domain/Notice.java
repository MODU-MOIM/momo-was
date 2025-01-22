package com.example.momowas.notice.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.vote.domain.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    @ColumnDefault("NULL")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_member_id")
    private CrewMember crewMember;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name="vote_id")
    @ColumnDefault("NULL")
    private Vote vote;

    @Builder
    public Notice(String content, LocalDateTime createdAt, Crew crew, CrewMember crewMember, Vote vote) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.content=content;
        this.crew= Objects.requireNonNull(crew,"crew는 null이 될 수 없습니다.");
        this.crewMember= Objects.requireNonNull(crewMember,"crewMember는 null이 될 수 없습니다.");
        this.vote= vote;
    }

    public void updateContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.content=content;
    }

    public void updateVote(Vote vote) {
        this.vote=Objects.requireNonNull(vote,"vote는 null이 될 수 없습니다.");
    }

    public void deleteVote() {
        if (this.vote != null) {
            this.vote = null;
        }
    }
}
