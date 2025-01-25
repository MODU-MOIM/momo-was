package com.example.momowas.archive.dto;

import com.example.momowas.archive.domain.Archive;
import com.example.momowas.comment.dto.CommentResDto;
import com.example.momowas.crew.domain.Role;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public record ArchiveDetailResDto(String title,
                                  String content,
                                  String writer,
                                  Role writerRole,
                                  String profileImage,
                                  LocalDateTime createdAt,
                                  int likeCount,
                                  boolean isLiked,
                                  int commentCount,
                                  List<CommentResDto> comments
                                  ) {
    public static ArchiveDetailResDto of(Archive archive, User user, boolean isLiked) {
        return new ArchiveDetailResDto(
                archive.getTitle(),
                archive.getContent(),
                user.getNickname(),
                archive.getCrewMember().getRole(),
                user.getProfileImage(),
                archive.getCreatedAt(),
                archive.getLikes().size(),
                isLiked,
                archive.getComments().size(),
                archive.getComments().stream()
                        .filter((comment -> comment.getParent()==null))
                        .map((comment ->
                                CommentResDto.of(comment, comment.getCrewMember().getUser())))
                        .toList()
        );
    }
}
