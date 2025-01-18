package com.example.momowas.vote.dto;

import com.example.momowas.vote.domain.Vote;

public record VoteRequest(boolean isEnabled,
                          String title) {
    public Vote toEntity() {
        return Vote.builder()
                .title(title)
                .build();
    }
}
