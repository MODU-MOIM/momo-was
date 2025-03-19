package com.example.momowas.notice.dto;

import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.user.domain.User;
import com.example.momowas.vote.dto.VoteDetailResDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record NoticeDetailResDto(Long noticeId,
                                 String writer,
                                 Role writerRole,
                                 String profileImage,
                                 LocalDateTime createdAt,
                                 String content,
                                 VoteDetailResDto vote) {

    public static NoticeDetailResDto of(User user, CrewMember crewMember, Notice notice, VoteDetailResDto voteDetailResDto) {
        return new NoticeDetailResDto(
                notice.getId(),
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                notice.getContent(),
                voteDetailResDto
        );
    }
}
