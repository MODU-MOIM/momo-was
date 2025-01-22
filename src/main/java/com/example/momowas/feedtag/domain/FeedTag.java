package com.example.momowas.feedtag.domain;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tag_id")
    private Tag tag;

    @Builder
    private FeedTag(Feed feed, Tag tag) {
        this.feed= Objects.requireNonNull(feed,"feed는 null이거나 빈 문자열이 될 수 없습니다.");
        this.tag= Objects.requireNonNull(tag,"tag는 null이거나 빈 문자열이 될 수 없습니다.");
    }

    public static FeedTag of(Feed feed, Tag tag) {
        return FeedTag.builder()
                .feed(feed)
                .tag(tag)
                .build();
    }

}
