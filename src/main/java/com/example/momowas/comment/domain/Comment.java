package com.example.momowas.comment.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_member_id")
    private CrewMember crewMember;

    @Builder
    public Comment(String content, Feed feed, CrewMember crewMember) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.content=content;
        this.feed= Objects.requireNonNull(feed,"feed는 null이거나 빈 문자열이 될 수 없습니다.");
        this.crewMember= Objects.requireNonNull(crewMember,"crewMember는 null이거나 빈 문자열이 될 수 없습니다.");
    }

    public void updateContent(String content) {
        this.content=content;
    }

    /* 작성자인지 검증 */
    public boolean isWriter(CrewMember crewMember) {
        return this.crewMember.getId()==crewMember.getId();
    }
}
