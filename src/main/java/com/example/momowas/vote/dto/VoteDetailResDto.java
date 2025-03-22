package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.domain.VoteType;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record VoteDetailResDto(boolean isEnabled,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               Long voteId,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               VoteType voteType,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               String title,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               VoteStatus voteStatus,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               Long positiveCount,
                               @JsonInclude(JsonInclude.Include.NON_NULL)
                               Long negativeCount){

    /* 투표 기능이 없는 경우 */
    public static VoteDetailResDto of() {
        return VoteDetailResDto.builder()
                .isEnabled(false)
                .build();
    }

    /* 투표 기능이 있는 경우 */
    public static VoteDetailResDto of(Vote vote, VoteType voteType, VoteStatus voteStatus) {
        return new VoteDetailResDto(
                true,
                vote.getId(),
                vote.getVoteType(),
                vote.getTitle(),
                voteStatus,
                vote.countPositiveParticipants(),
                vote.countNegativeParticipants()
        );
    }

    public static VoteDetailResDto of(Vote vote, VoteStatus voteStatus) {
        VoteDetailResDto voteDetailResDto=null;

        if (vote.getVoteType()==VoteType.ATTENDANCE) {
            voteDetailResDto=VoteDetailResDto.of(vote, VoteType.ATTENDANCE, voteStatus);
        }
        if (vote.getVoteType()==VoteType.GENERAL) {
            voteDetailResDto=VoteDetailResDto.of(vote, VoteType.GENERAL, voteStatus);
        }

        return voteDetailResDto;
    }

}

