package com.example.momowas.feed.dto;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;

public record FeedListResDto(Long feedId,
                             String writer,
                             String profileImage,
                             LocalDateTime createdAt,
                             String thumbnailImage,
                             String content) { //좋아요 기능 구현 후 사용자가 좋아요 눌렀는지 여부도 필요
    public static FeedListResDto of(Feed feed, User user) {
        return new FeedListResDto(
                feed.getId(),
                user.getNickname(),
                user.getProfileImage(),
                feed.getCreatedAt(),
                feed.getPhotos().get(0).getUrl(),
                feed.getContent()
        );
    }
}
