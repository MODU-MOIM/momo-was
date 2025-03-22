package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.domain.VoteType;
import com.fasterxml.jackson.annotation.JsonInclude;

public record VoteListResDto(boolean isEnabled,
                             @JsonInclude(JsonInclude.Include.NON_NULL)
                             VoteType voteType,
                             @JsonInclude(JsonInclude.Include.NON_NULL)
                             Long voteParticipantsCount,
                             @JsonInclude(JsonInclude.Include.NON_NULL)
                             Long attendingCount) {

    private static VoteListResDto of(boolean isEnabled, VoteType voteType,  Long voteParticipantsCount, Long attendingCount) {
        return new VoteListResDto(isEnabled, voteType, voteParticipantsCount, attendingCount);
    }

    /* 투표 타입에 따른 VoteListResDto 생성 */
    public static VoteListResDto of(Vote vote) {
        VoteListResDto voteListResDto = null;

        if (vote == null) {
            voteListResDto = VoteListResDto.of(false, null, null, null);
        }
        else if (vote.getVoteType() == VoteType.ATTENDANCE) {
            voteListResDto = VoteListResDto.of(true, VoteType.ATTENDANCE, null, vote.countPositiveParticipants());
        }
        else if (vote.getVoteType() == VoteType.GENERAL) {
            voteListResDto = VoteListResDto.of(true, VoteType.GENERAL, vote.countParticipants(), null);
        }

        return voteListResDto;
    }
}

/*
* VoteType이 ATTENDANCE -> attendingCount 활성화('참석'에 투표한 투표 참여자 수)
* VoteType이이 GENERAL -> voteParticipantsCount 활성화(전체 투표 참여자 수)
*
* */