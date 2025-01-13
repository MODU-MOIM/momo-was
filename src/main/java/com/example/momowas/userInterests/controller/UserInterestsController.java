package com.example.momowas.userInterests.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.userInterests.dto.CategorySelectDto;
import com.example.momowas.userInterests.service.UserInterestsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/interests")
@RequiredArgsConstructor
public class UserInterestsController {
    //interests
    private final JwtUtil jwtUtil;
    private final UserInterestsService userInterestsService;

    @PostMapping("")
    public CommonResponse<String> createInterests(HttpServletRequest request, @RequestBody CategorySelectDto categorySelectDto){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        userInterestsService.createInterests(userId, categorySelectDto);

        return CommonResponse.of(ExceptionCode.SUCCESS, "관심사 등록 성공");
    }


}
