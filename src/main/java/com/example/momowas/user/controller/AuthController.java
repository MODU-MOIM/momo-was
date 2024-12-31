package com.example.momowas.user.controller;

import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.SignUpReq;
import com.example.momowas.user.dto.SmsReqDto;
import com.example.momowas.user.dto.ValidationCodeReq;
import com.example.momowas.user.service.AuthService;
import com.example.momowas.user.service.UserService;
import com.example.momowas.user.util.SmsUtil;
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
    private final SmsUtil smsUtil;

    @PostMapping("/sign-up")
    public Long signUp(@RequestBody SignUpReq signUpReq) {
        User user = authService.signUp(signUpReq);
        userService.create(user);
        return user.getId();
    }

    @PostMapping("/send-sms")
    public SingleMessageSentResponse sendSMS(@RequestBody @Valid SmsReqDto smsReqDto, HttpSession ss) {
        String verificationCode = smsUtil.generateRandomNumber();
        String toPhoneNumber = smsReqDto.getToPhoneNumber();
        SingleMessageSentResponse response = smsUtil.sendOne(toPhoneNumber, verificationCode);
        ss.setAttribute("validation", verificationCode);
        ss.setAttribute("message_id", response.getMessageId());
        ss.setMaxInactiveInterval(180);
        return response;
    }

    @PostMapping("/verify-code")
    public String validationCode(@RequestBody ValidationCodeReq validationCodeReq, HttpSession ss) {
        String verificationCode = validationCodeReq.getVerificationCode();
        return smsUtil.validationCode(verificationCode, ss);
    }
}
