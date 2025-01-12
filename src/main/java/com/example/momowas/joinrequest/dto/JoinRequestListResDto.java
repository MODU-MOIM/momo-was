package com.example.momowas.joinrequest.dto;

import com.example.momowas.user.domain.Gender;
import com.example.momowas.user.domain.User;

public record JoinRequestListResDto(String nickname,
                                    Gender gender,
                                    Integer age,
                                    String profileImage) {
    public static JoinRequestListResDto of(User user) {
        return new JoinRequestListResDto(
                user.getNickname(),
                user.getGender(),
                user.getAge(),
                user.getProfileImage()
        );
    }
}
