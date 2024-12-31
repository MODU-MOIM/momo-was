package com.example.momowas.user.dto;

import lombok.Data;

@Data
public class SignUpReqDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String cp;
}
