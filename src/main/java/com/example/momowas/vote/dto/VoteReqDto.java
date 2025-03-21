package com.example.momowas.vote.dto;

import com.example.momowas.notice.domain.Notice;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.domain.VoteType;

public record VoteReqDto(boolean isEnabled,
                         VoteType voteType,
                         String title) {
    public Vote toEntity() {
        return Vote.builder()
                .voteType(voteType)
                .title(title)
                .build();
    }
}
