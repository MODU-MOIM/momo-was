package com.example.momowas.jwt.dto;

import lombok.Data;

@Data
public class ReIssueTokenDto {
    private final String expiredAccessToken;
}
