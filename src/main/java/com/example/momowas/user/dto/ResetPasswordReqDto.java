package com.example.momowas.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ResetPasswordReqDto {
    String email;
    String newPassword;
}
