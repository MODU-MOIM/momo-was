package com.example.momowas.user.service;

import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.jwt.service.RefreshTokenService;
import com.example.momowas.user.dto.*;
import com.example.momowas.user.domain.User;
import io.lettuce.core.ConnectionEvents;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.refreshExpiration}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;


    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public UserDto signUp(SignUpReqDto signUpReqDto){
        if (userService.isEmailExists(signUpReqDto.getEmail())) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS);
        }

        String enPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        //사용자 기본 설정
        User user =  User.builder()
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
        userService.create(user);
        return UserDto.fromEntity(user);
    }

    public void signIn(SignInReqDto signInReqDto, HttpServletResponse response){
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

        response.setHeader("Authorization", "Bearer " + accessToken);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRATION_TIME);
        response.addCookie(cookie);
    }

    public boolean verifyMe(Long userId, CheckingPasswordReqDto checkingPassowordReqDto){
        User user = userService.findUserById(userId);

        return passwordEncoder.matches(checkingPassowordReqDto.getPassword(), user.getPassword());
    }

    public FindEmailResDto findEmail(SmsReqDto smsReqDto){
        User user = userService.findUserByCp(smsReqDto.getToPhoneNumber());
        return FindEmailResDto.builder()
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void resetPassword(ResetPasswordReqDto resetPasswordReqDto){
        User user = userService.findUserByEmail(resetPasswordReqDto.getEmail());
        String enPassword = passwordEncoder.encode(resetPasswordReqDto.getNewPassword());

        user.updatePassword(enPassword);
    }

}
