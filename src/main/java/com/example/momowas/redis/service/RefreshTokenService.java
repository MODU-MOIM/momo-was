package com.example.momowas.redis.service;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.redis.domain.RefreshToken;
import com.example.momowas.redis.repository.RefreshTokenRepository;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public void create(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }
}