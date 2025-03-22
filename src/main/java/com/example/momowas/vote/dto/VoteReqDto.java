package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.domain.VoteType;
import lombok.Builder;

@Builder
public record VoteReqDto(boolean isEnabled,
                         VoteType voteType,
                         String title
) {
    public Vote toEntity() {
        return Vote.builder()
                .voteType(voteType)
                .title(title)
                .build();
    }

    public static VoteReqDto of(boolean isEnabled, VoteType voteType, String title) {
        return VoteReqDto
                .builder()
                .isEnabled(isEnabled)
                .voteType(voteType)
                .title(title)
                .build();
    }
}
