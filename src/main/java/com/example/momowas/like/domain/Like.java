package com.example.momowas.like.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name="likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_member_id")
    private CrewMember crewMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    private Like(Feed feed, CrewMember crewMember) {
        this.feed= Objects.requireNonNull(feed,"feed는 null이 될 수 없습니다.");
        this.crewMember= Objects.requireNonNull(crewMember,"crewMember는 null이 될 수 없습니다.");
    }

    public static Like of(Feed feed, CrewMember crewMember) {
        return Like.builder()
                .feed(feed)
                .crewMember(crewMember)
                .build();
    }
}
