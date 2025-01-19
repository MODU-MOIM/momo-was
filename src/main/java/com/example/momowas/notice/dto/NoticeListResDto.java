
package com.example.momowas.notice.dto;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record NoticeListResDto(Long noticeId,
                               String writer,
                               Role writerRole,
                               String profileImage,
                               LocalDateTime createdAt,
                               String content,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               Long attendingCounts
                               ) {
    public static NoticeListResDto of(User user, CrewMember crewMember, Notice notice) {
        return new NoticeListResDto(
                notice.getId(),
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                notice.getContent(),
                notice.getVote()!=null ? notice.getVote().countAttendingParticipants() : null
        );
    }
}

