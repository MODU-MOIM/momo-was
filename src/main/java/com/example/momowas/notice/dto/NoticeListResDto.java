
package com.example.momowas.notice.dto;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.user.domain.User;

import java.time.LocalDateTime;

public record NoticeListResDto(String writer,
                               Role writerRole,
                               String profileImage,
                               LocalDateTime createdAt,
                               String content
                               //Integer participantCount
                               ) {
    public static NoticeListResDto of(User user, CrewMember crewMember, Notice notice) {
        return new NoticeListResDto(
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                notice.getContent()
        );
    }
}

