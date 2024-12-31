package com.example.momowas.user.dto;

import lombok.Data;

@Data
public class SignUpReq {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String cp;
}
