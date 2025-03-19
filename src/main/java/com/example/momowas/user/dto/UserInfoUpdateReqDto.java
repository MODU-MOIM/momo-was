package com.example.momowas.user.dto;

import com.example.momowas.user.domain.Gender;
import lombok.Data;


@Data
public class UserInfoUpdateReqDto {
    private final String nickname;
    private final String cp;
    private final Gender gender;
    private final int age;
}
