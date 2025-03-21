package com.example.momowas.voteparticipant.dto;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;

public record VoteParticipantReqDto(VoteStatus voteStatus) {
    public VoteParticipant toEntity(Vote vote,
                                    CrewMember crewMember) {
        return VoteParticipant.builder()
                .status(voteStatus)
                .vote(vote)
                .crewMember(crewMember)
                .build();
    }
}
