package com.example.momowas.user.service;

import com.example.momowas.user.dto.SignUpReqDto;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpReqDto signUpReqDto){
        String enPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        //사용자 기본 설정
        return User.builder()
                .email(signUpReqDto.getEmail())
                .password(enPassword)
                .nickname(signUpReqDto.getNickname())
                .cp(signUpReqDto.getCp())
                .score(0)
                .createdAt(LocalDateTime.now())
                .gender(null)
                .age(0)
                .profileImage(null)
                .build();
    }

}
