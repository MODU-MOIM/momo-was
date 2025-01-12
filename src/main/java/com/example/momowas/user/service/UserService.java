package com.example.momowas.user.service;

import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public User readById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    public void create(User user){
        userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    public User findUserByProviderId(String providerId){
        return userRepository.findByProviderId(providerId);
    }


    //프로필 업로드
    public String updateProfileImage(Long userId, MultipartFile file) throws IOException {
        User user = readById(userId);
        String fileUrl = s3Service.uploadImage(file, "profile");

        user.updateProfileImage(fileUrl);
        return fileUrl;
    }



}
