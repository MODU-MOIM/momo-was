package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.fasterxml.jackson.annotation.JsonInclude;

public record VoteDetailResDto(boolean isEnabled,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               Long voteId,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               String title,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               VoteStatus voteStatus) {
    public static VoteDetailResDto of(boolean isEnabled, Vote vote, VoteParticipant voteParticipant) {
        return new VoteDetailResDto(
                isEnabled,
                vote!=null ? vote.getId() : null,
                vote!=null ? vote.getTitle() : null,
                vote==null ? null : (voteParticipant!=null ? voteParticipant.getStatus() : VoteStatus.NOT_VOTED)
        );
    }
}


