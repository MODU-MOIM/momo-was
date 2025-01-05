package com.example.momowas.user.service;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.jwt.dto.JwtTokenDto;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.redis.service.RefreshTokenService;
import com.example.momowas.user.dto.SignInReqDto;
import com.example.momowas.user.dto.SignUpReqDto;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public User signUp(SignUpReqDto signUpReqDto){
        String enPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        //사용자 기본 설정
        return User.builder()
                .email(signUpReqDto.getEmail())
                .password(enPassword)
                .nickname(signUpReqDto.getNickname())
                .cp(signUpReqDto.getCp())
                .score(0)
                .createdAt(LocalDateTime.now())
                .gender(null)
                .age(0)
                .profileImage(null)
                .provider("momo")
                .providerId(signUpReqDto.getEmail())
                .build();
    }

    public JwtTokenDto signIn(SignInReqDto signInReqDto){
        String email = signInReqDto.getEmail();
        User user = userService.findUserByEmail(email);

        if (!passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        // 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        //redis refresh Token 저장
        refreshTokenService.saveTokenInfo(String.valueOf(user.getId()), refreshToken, accessToken);

        return JwtTokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
