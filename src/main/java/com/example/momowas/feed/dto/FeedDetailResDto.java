package com.example.momowas.feed.dto;

import com.example.momowas.comment.dto.FeedCommentResDto;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.photo.dto.PhotoResDto;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public record FeedDetailResDto(Long feedId,
                               String writer,
                               String profileImage,
                               String content,
                               LocalDateTime createdAt,
                               List<String> tags,
                               List<PhotoResDto> photos,
                               List<FeedCommentResDto> comments,
                               int likeCount,
                               boolean isLiked) {
    public static FeedDetailResDto of(Feed feed, User writer, boolean isLiked) {
        return new FeedDetailResDto(
                feed.getId(),
                writer.getNickname(),
                writer.getProfileImage(),
                feed.getContent(),
                feed.getCreatedAt(),
                feed.getFeedTags().stream()
                        .map(feedTag -> feedTag.getTag().getName())
                        .toList(),
                feed.getPhotos().stream()
                        .map(PhotoResDto::of).toList(),
                feed.getComments().stream()
                        .filter((comment -> comment.getParent()==null))
                        .map((comment ->
                        FeedCommentResDto.of(comment, comment.getCrewMember().getUser())))
                        .toList(),
                feed.getLikes().size(),
                isLiked
        );
    }
}

