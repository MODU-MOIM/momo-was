package com.example.momowas.user.service;

import com.example.momowas.user.dto.SignUpReq;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpReq signUpReq){
        String enPassword = passwordEncoder.encode(signUpReq.getPassword());

        //사용자 기본 설정
        return User.builder()
                .email(signUpReq.getEmail())
                .password(enPassword)
                .nickname(signUpReq.getNickname())
                .cp(signUpReq.getCp())
                .score(0)
                .createdAt(LocalDateTime.now())
                .gender(null)
                .age(0)
                .profileImage(null)
                .build();
    }

}
