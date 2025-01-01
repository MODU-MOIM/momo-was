package com.example.momowas.jwt.util;

import com.example.momowas.user.service.UserService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        } catch (Exception e) {
        }
        return false;
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

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

}