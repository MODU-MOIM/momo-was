package com.example.momowas.user.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.dto.SignInReqDto;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.dto.UserInfoUpdateReqDto;
import com.example.momowas.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/upload-profile")
    public CommonResponse<String>  updateProfileImage(HttpServletRequest request,  @RequestPart(value = "profileImage", required = false) MultipartFile file) throws IOException {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        String fileUrl =  userService.updateProfileImage(userId, file);
        return CommonResponse.of(ExceptionCode.SUCCESS, fileUrl);

    }
    @GetMapping("/me")
    public UserDto getMyInfo(HttpServletRequest request){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return userService.getMyInfo(userId);
    }
    @PutMapping("")
    public UserDto updateUserInfo(HttpServletRequest request, @RequestBody UserInfoUpdateReqDto userInfoUpdateReqDto){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return userService.updateMyInfo(userId, userInfoUpdateReqDto);
    }
}
