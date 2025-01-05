package com.example.momowas.jwt.service;

<<<<<<< HEAD:src/main/java/com/example/momowas/jwt/service/RefreshTokenService.java
import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.jwt.dto.ReIssueTokenDto;
=======
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.jwt.dto.JwtTokenDto;
>>>>>>> 3e1a5dd (fix: 성공 응답):src/main/java/com/example/momowas/redis/service/RefreshTokenService.java
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.jwt.domain.RefreshToken;
import com.example.momowas.jwt.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    public void reissueAccessToken(ReIssueTokenDto reIssueTokenDto, HttpServletResponse response) {
        RefreshToken foundTokenInfo  = refreshTokenRepository.findByAccessToken(reIssueTokenDto.getExpiredAccessToken()).orElseThrow(() -> new BusinessException(ExceptionCode.TOKEN_MISSING));
        log.info(foundTokenInfo.getRefreshToken());
        String refreshToken = foundTokenInfo.getRefreshToken();

        if(jwtUtil.validateToken(refreshToken)==null){
            Long userId = Long.valueOf(foundTokenInfo.getId());

            String newAccessToken = jwtUtil.generateAccessToken(userId);
            foundTokenInfo.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(foundTokenInfo);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRATION_TIME);

        }
    }
}