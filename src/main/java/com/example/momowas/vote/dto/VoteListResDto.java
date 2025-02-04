package com.example.momowas.vote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record VoteListResDto(boolean isEnabled,
                             @JsonInclude(JsonInclude.Include.NON_NULL)
                             Long attendingCounts) {
    public static VoteListResDto of(boolean isEnabled, Long attendingCounts) {
        return new VoteListResDto(isEnabled, attendingCounts);
    }
}
