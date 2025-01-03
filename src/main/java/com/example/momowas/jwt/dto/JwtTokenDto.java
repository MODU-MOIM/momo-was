package com.example.momowas.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtTokenDto(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
