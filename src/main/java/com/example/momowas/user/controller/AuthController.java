package com.example.momowas.user.controller;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.SignUpReqDto;
import com.example.momowas.user.dto.SmsReqDto;
import com.example.momowas.user.dto.ValidationCodeReqDto;
import com.example.momowas.user.service.AuthService;
import com.example.momowas.user.service.UserService;
import com.example.momowas.user.util.SmsUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
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
}
