
package com.example.momowas.notice.dto;

import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.user.domain.User;
import com.example.momowas.vote.dto.VoteListResDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record NoticeListResDto(Long noticeId,
                               String writer,
                               Role writerRole,
                               String profileImage,
                               LocalDateTime createdAt,
                               Object content,
                               VoteListResDto vote,
                               boolean isPinned
                               ) {
    public static NoticeListResDto of(User user, CrewMember crewMember, Notice notice, VoteListResDto vote) {
        return new NoticeListResDto(
                notice.getId(),
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                notice.getContent(),
                vote,
                notice.getIsPinned()
        );
    }

    public static NoticeListResDto of(User user, CrewMember crewMember, Notice notice, VoteListResDto vote, ScheduleDto scheduleDto) {
        return new NoticeListResDto(
                notice.getId(),
                user.getNickname(),
                crewMember.getRole(),
                user.getProfileImage(),
                notice.getCreatedAt(),
                scheduleDto,
                vote,
                notice.getIsPinned()
        );
    }
}

