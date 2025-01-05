package com.example.momowas.redis.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "Token")
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String accessToken;

    private long expirationTime;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
