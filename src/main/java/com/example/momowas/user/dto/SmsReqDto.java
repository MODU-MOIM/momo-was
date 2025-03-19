package com.example.momowas.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SmsReqDto {
    @NotEmpty(message = "휴대폰 번호를 입력해주세요")
    private String toPhoneNumber;
}