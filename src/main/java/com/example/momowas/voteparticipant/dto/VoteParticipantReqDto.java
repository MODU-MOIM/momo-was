package com.example.momowas.voteparticipant.dto;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.voteparticipant.domain.AttendanceStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;

public record VoteParticipantReqDto(AttendanceStatus attendanceStatus) {
    public VoteParticipant toEntity(Vote vote,
                                    CrewMember crewMember) {
        return VoteParticipant.builder()
                .status(attendanceStatus)
                .vote(vote)
                .crewMember(crewMember)
                .build();
    }
}
