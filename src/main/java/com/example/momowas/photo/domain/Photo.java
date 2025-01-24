package com.example.momowas.photo.domain;

import com.example.momowas.feed.domain.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Integer sequence;

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed;

    @CreatedDate
    private LocalDateTime uploadedAt;

    @Builder
    private Photo(String url,
                  Integer sequence,
                  Feed feed
    ) {
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("url은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.url=url;
        this.sequence= Objects.requireNonNull(sequence,"sequence는 null이 될 수 없습니다.");
        this.feed= Objects.requireNonNull(feed,"feed는 null이 될 수 없습니다.");
    }

    public static Photo of(String url,
                         Integer sequence,
                         Feed feed) {
        return Photo.builder()
                .url(url)
                .sequence(sequence)
                .feed(feed)
                .build();
    }

}
