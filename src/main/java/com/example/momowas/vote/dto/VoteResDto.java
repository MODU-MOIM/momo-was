package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.voteparticipant.domain.AttendanceStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.fasterxml.jackson.annotation.JsonInclude;

public record VoteResDto(Long voteId,
                         String title,
                         @JsonInclude(JsonInclude.Include.NON_NULL)
                         AttendanceStatus attendanceStatus) {
    public static VoteResDto of(Vote vote, VoteParticipant voteParticipant) {
        return new VoteResDto(
                vote.getId(),
                vote.getTitle(),
                voteParticipant!=null ? voteParticipant.getStatus() : null
        );
    }
}
