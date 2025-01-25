package com.example.momowas.like.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.archive.domain.Archive;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @Builder(builderMethodName = "feedLikeBuilder", builderClassName="feedLikeBuilder")
    private Like(Feed feed, CrewMember crewMember) {
        this.feed= Objects.requireNonNull(feed,"feed는 null이 될 수 없습니다.");
        this.archive=null;
        this.crewMember= Objects.requireNonNull(crewMember,"crewMember는 null이 될 수 없습니다.");
    }

    @Builder(builderMethodName = "archiveLikeBuilder", builderClassName="archiveLikeBuilder")
    private Like(Archive archive, CrewMember crewMember) {
        this.archive= Objects.requireNonNull(archive,"archive는 null이 될 수 없습니다.");
        this.feed=null;
        this.crewMember= Objects.requireNonNull(crewMember,"crewMember는 null이 될 수 없습니다.");
    }

    public static Like of(Feed feed, CrewMember crewMember) {
        return Like.feedLikeBuilder()
                .feed(feed)
                .crewMember(crewMember)
                .build();
    }

    public static Like of(Archive archive, CrewMember crewMember) {
        return Like.archiveLikeBuilder()
                .archive(archive)
                .crewMember(crewMember)
                .build();
    }
}
