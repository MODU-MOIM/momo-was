package com.example.momowas.feed.dto;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;

public record FeedListResDto(Long feedId,
                             String writer,
                             String profileImage,
                             LocalDateTime createdAt,
                             String thumbnailImage,
                             String content,
                             int likeCount,
                             boolean isLiked) {
    public static FeedListResDto of(Feed feed, User user, boolean isLiked) {
        return new FeedListResDto(
                feed.getId(),
                user.getNickname(),
                user.getProfileImage(),
                feed.getCreatedAt(),
                feed.getPhotos().get(0).getUrl(),
                feed.getContent(),
                feed.getLikes().size(),
                isLiked
        );
    }
}
