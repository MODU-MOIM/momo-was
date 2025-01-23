package com.example.momowas.comment.dto;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;

public record FeedCommentResDto(Long commentId,
                                String writer,
                                String profileImage,
                                String content,
                                LocalDateTime createdAt) {
    public static FeedCommentResDto of(Comment comment, User user) {
        return new FeedCommentResDto(
                comment.getId(),
                user.getNickname(),
                user.getProfileImage(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
