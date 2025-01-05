package com.example.momowas.user.controller;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.jwt.dto.JwtTokenDto;
import com.example.momowas.jwt.dto.ReIssueTokenDto;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.redis.service.RefreshTokenService;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.SignInReqDto;
import com.example.momowas.user.dto.SignUpReqDto;
import com.example.momowas.user.dto.SmsReqDto;
import com.example.momowas.user.dto.ValidationCodeReqDto;
import com.example.momowas.user.service.AuthService;
import com.example.momowas.user.service.UserService;
import com.example.momowas.user.util.SmsUtil;
import com.nimbusds.oauth2.sdk.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService tokenService;
    private final JwtUtil jwtUtil;
    private final SmsUtil smsUtil;

    @PostMapping("/sign-up")
    public Long signUp(@RequestBody @Valid SignUpReqDto signUpReqDto, HttpSession session) {
        // 이메일 중복 체크
        if (userService.isEmailExists(signUpReqDto.getEmail())) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS);
        }

        // 인증 여부 확인 -> sms 유료라서 일단 주석하겠습니다!
//        if (!smsUtil.isAuthenticated(session)) {
//            throw new BusinessException(ExceptionCode.NOT_VERIFIED_YET);
//        }

        User user = authService.signUp(signUpReqDto);
        userService.create(user);
        return user.getId();
    }

    @PostMapping("/send-sms")
    public SingleMessageSentResponse sendSMS(@RequestBody @Valid SmsReqDto smsReqDto, HttpSession session) {
        try {
            return smsUtil.sendOne(smsReqDto.getToPhoneNumber(), session);
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.SMS_SEND_FAILED);
        }
    }

    @PostMapping("/verify-code")
    public String validationCode(@RequestBody @Valid ValidationCodeReqDto validationCodeReqDto, HttpSession session) {
        // 이미 인증됨
        if (smsUtil.isAuthenticated(session)) {
            throw new BusinessException(ExceptionCode.ALREADY_AUTHENTICATED);
        }
        boolean isValid = smsUtil.validateCode(validationCodeReqDto.getVerificationCode(), session);
        if (isValid) {
            return "인증 완료";
        } else {
            throw new BusinessException(ExceptionCode.INVALID_VERIFICATION_CODE);
        }
    }

    @PostMapping("/sign-out")
    public void logout(@RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
    }

    @PostMapping("/reissue")
    public void reissueAccessToken(
            @RequestBody ReIssueTokenDto reIssueTokenDto, HttpServletResponse response) {
        tokenService.reissueAccessToken(reIssueTokenDto, response);
    }

    @PostMapping("/sign-in")
    public void reissueAccessToken(@RequestBody SignInReqDto signInReqDto, HttpServletResponse response) {
        authService.signIn(signInReqDto, response);
    }


}
