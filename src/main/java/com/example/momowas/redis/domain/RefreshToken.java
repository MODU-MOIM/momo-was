package com.example.momowas.redis.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "Token")
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

    @Builder
    public RefreshToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
