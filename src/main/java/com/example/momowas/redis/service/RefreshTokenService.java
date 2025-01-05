package com.example.momowas.redis.service;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.jwt.dto.JwtTokenDto;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.redis.domain.RefreshToken;
import com.example.momowas.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refreshExpiration}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    public void saveTokenInfo(String userId, String refreshToken, String accessToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken, accessToken, REFRESH_TOKEN_EXPIRATION_TIME));
    }

    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(ExceptionCode.INVALID_TOKEN));
        refreshTokenRepository.delete(token);
    }

    public JwtTokenDto reissueAccessToken(String accessToken) {
        // redis에 저장되어있는 토큰 정보를 만료된 access token으로 찾아온다.
        RefreshToken foundTokenInfo  = refreshTokenRepository.findByAccessToken(accessToken).orElseThrow(() -> new BusinessException(ExceptionCode.TOKEN_MISSING));
        log.info(foundTokenInfo.getRefreshToken());
        String refreshToken = foundTokenInfo.getRefreshToken();

        if(jwtUtil.validateToken(refreshToken)){
            Long userId = Long.valueOf(foundTokenInfo.getId());

            String newAccessToken = jwtUtil.generateAccessToken(userId);
            foundTokenInfo.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(foundTokenInfo);

            return JwtTokenDto.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }
}