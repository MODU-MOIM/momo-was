package com.example.momowas.notice.dto;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.user.domain.User;
import com.example.momowas.vote.dto.VoteResDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record NoticeDetailResDto(String writer,
                                 Role writerRole,
                                 String profileImage,
                                 LocalDateTime createdAt,
                                 String content,
                                 @JsonInclude(JsonInclude.Include.NON_NULL)
                                 VoteResDto vote) {

    public static NoticeDetailResDto of(User user, CrewMember crewMember, Notice notice, VoteResDto voteResDto) {
        return new NoticeDetailResDto(
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                notice.getContent(),
                voteResDto
        );
    }
}
