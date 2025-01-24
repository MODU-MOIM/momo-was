package com.example.momowas.comment.dto;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public record FeedCommentResDto(Long commentId,
                                String writer,
                                String profileImage,
                                String content,
                                LocalDateTime createdAt,
                                @JsonInclude(JsonInclude.Include.NON_EMPTY)
                                List<FeedCommentResDto> replies) {
    public static FeedCommentResDto of(Comment comment, User user) {
        return new FeedCommentResDto(
                comment.getId(),
                user.getNickname(),
                user.getProfileImage(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getReplies().stream()
                        .map((reply)->FeedCommentResDto.of(reply, reply.getCrewMember().getUser()))
                        .toList()
        );
    }
}
