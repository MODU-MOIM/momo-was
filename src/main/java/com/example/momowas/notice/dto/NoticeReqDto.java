package com.example.momowas.notice.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.notice.domain.NoticeType;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteReqDto;
import lombok.Builder;

@Builder
public record NoticeReqDto(
        String content,
        VoteReqDto vote
){
    public Notice toEntity(Crew crew, CrewMember crewMember, Vote vote, NoticeType noticeType, Schedule schedule) {
        return Notice.builder()
                .content(content)
                .crew(crew)
                .crewMember(crewMember)
                .vote(vote)
                .noticeType(noticeType)
                .schedule(schedule)
                .build();
    }

    public static NoticeReqDto of(String content, VoteReqDto voteReqDto) {
        return NoticeReqDto
                .builder()
                .content(content)
                .vote(voteReqDto)
                .build();
    }
}
