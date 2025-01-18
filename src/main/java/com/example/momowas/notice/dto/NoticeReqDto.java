package com.example.momowas.notice.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.notice.domain.Notice;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteRequest;


public record NoticeReqDto(
        String content,
        VoteRequest vote
        ){
    public Notice toEntity(Crew crew, CrewMember crewMember, Vote vote) {
        return Notice.builder()
                .content(content)
                .crew(crew)
                .crewMember(crewMember)
                .vote(vote)
                .build();
    }
}
