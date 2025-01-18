package com.example.momowas.notice.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.vote.domain.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @OneToOne
    @JoinColumn(name="vote_id")
    private Vote vote;

    @Builder
    public Notice(String content, LocalDateTime createdAt, Crew crew, Vote vote) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.content=content;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt는 null이 될 수 없습니다.");
        this.crew= Objects.requireNonNull(crew,"crew는 null이 될 수 없습니다.");
        this.vote= vote;
    }
}
