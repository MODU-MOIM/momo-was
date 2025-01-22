package com.example.momowas.user.controller;


import com.example.momowas.jwt.dto.ReIssueTokenDto;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.jwt.service.RefreshTokenService;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.*;
import com.example.momowas.user.service.AuthService;
import com.example.momowas.user.service.UserService;
import com.example.momowas.user.util.SmsUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService tokenService;
    private final SmsUtil smsUtil;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public UserDto signUp(@RequestBody @Valid SignUpReqDto signUpReqDto, HttpSession session) {
        // 이메일 중복 체크
        if (userService.isEmailExists(signUpReqDto.getEmail())) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS);
        }

        // 인증 여부 확인
//        if (!smsUtil.isAuthenticated(session)) {
//            throw new BusinessException(ExceptionCode.NOT_VERIFIED_YET);
//        }

        User user = authService.signUp(signUpReqDto);
        userService.create(user);

        return UserDto.fromEntity(user);
    }

    @PostMapping("/send-sms")
    public CommonResponse<String> sendSMS(@RequestBody @Valid SmsReqDto smsReqDto, HttpSession session) {
        try {
            smsUtil.sendOne(smsReqDto.getToPhoneNumber(), session);
            return CommonResponse.of(ExceptionCode.SUCCESS, "SMS 전송 성공");
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.SMS_SEND_FAILED);
        }
    }

    @PostMapping("/verify-code")
    public CommonResponse<String> validationCode(@RequestBody @Valid ValidationCodeReqDto validationCodeReqDto, HttpSession session) {
        // 이미 인증됨
        if (smsUtil.isAuthenticated(session)) {
            throw new BusinessException(ExceptionCode.ALREADY_AUTHENTICATED);
        }
        boolean isValid = smsUtil.validateCode(validationCodeReqDto.getVerificationCode(), session);
        if (isValid) {
            return CommonResponse.of(ExceptionCode.SUCCESS, "인증 완료");
        } else {
            throw new BusinessException(ExceptionCode.INVALID_VERIFICATION_CODE);
        }
    }

    @PostMapping("/sign-out")
    public  CommonResponse<String> logout(HttpServletRequest request) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(jwtUtil.resolveToken(request).substring(7));
        return CommonResponse.of(ExceptionCode.SUCCESS, "로그아웃 성공");
    }

    @PostMapping("/reissue")
    public CommonResponse<String> reissueAccessToken(
            @RequestBody ReIssueTokenDto reIssueTokenDto, HttpServletResponse response) {
        tokenService.reissueAccessToken(reIssueTokenDto, response);
        return CommonResponse.of(ExceptionCode.SUCCESS, "토큰 재발행 성공");
    }

    @PostMapping("/sign-in")
    public CommonResponse<String> signIn(@RequestBody SignInReqDto signInReqDto, HttpServletResponse response) {
        authService.signIn(signInReqDto, response);
        return CommonResponse.of(ExceptionCode.SUCCESS, "로그인 성공");
    }


}
