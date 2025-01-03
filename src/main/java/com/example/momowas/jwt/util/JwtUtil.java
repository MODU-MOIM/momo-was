package com.example.momowas.jwt.util;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.redis.domain.RefreshToken;
import com.example.momowas.redis.service.RefreshTokenService;
import com.example.momowas.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Ref;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {
    private final String secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;
    private final UserService userService;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.accessExpiration}") long accessExpiration,
            @Value("${jwt.refreshExpiration}") long refreshExpiration,
            @Value("${jwt.issuer}") String issuer,
            UserService userService
    ) {
        this.secretKey = secretKey;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
        this.userService = userService;
    }


    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    public String generateRefreshToken(Long userId){
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            if (userService.read(claims.getBody().get("userId", Long.class)) == null) {
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ExceptionCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.INVALID_TOKEN);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public String getTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

}