package com.example.momowas.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ValidationCodeReq {
    @NotEmpty(message = "인증번호를 입력해주세요")
    private String verificationCode;
}
