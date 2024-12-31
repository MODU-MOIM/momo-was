package com.example.momowas.user.dto;

import com.example.momowas.user.domain.Gender;
import com.example.momowas.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String cp;
    private final double score;
    private final LocalDateTime createdAt;
    private final Gender gender;
    private final int age;
    private final String profileImage;

    @Builder
    public UserDto(Long id, String email, String nickname, String cp, double score, LocalDateTime createdAt, Gender gender, int age, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.cp = cp;
        this.score = score;
        this.createdAt = createdAt;
        this.gender = gender;
        this.age = age;
        this.profileImage = profileImage;
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .cp(user.getCp())
                .score(user.getScore())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .age(user.getAge())
                .profileImage(user.getProfileImage())
                .build();
    }
}
