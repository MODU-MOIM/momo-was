package com.example.momowas.vote.dto;

import com.example.momowas.notice.domain.Notice;
import com.example.momowas.vote.domain.Vote;

public record VoteReqDto(boolean isEnabled,
                         String title) {
    public Vote toEntity() {
        return Vote.builder()
                .title(title)

                .build();
    }
}
