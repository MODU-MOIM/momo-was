package com.example.momowas.comment.dto;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResDto(Long commentId,
                            String writer,
                            String profileImage,
                            String content,
                            LocalDateTime createdAt,
                            @JsonInclude(JsonInclude.Include.NON_EMPTY)
                                List<CommentResDto> replies) {
    public static CommentResDto of(Comment comment, User user) {
        return new CommentResDto(
                comment.getId(),
                user.getNickname(),
                user.getProfileImage(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getReplies().stream()
                        .map((reply)-> CommentResDto.of(reply, reply.getCrewMember().getUser()))
                        .toList()
        );
    }
}
