package com.example.momowas.user.controller;

import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.SignUpReq;
import com.example.momowas.user.service.AuthService;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(@RequestBody SignUpReq signUpReq) {
        User user = authService.signUp(signUpReq);
        userService.create(user);
        return ResponseEntity.ok(user.getId());
    }
}
