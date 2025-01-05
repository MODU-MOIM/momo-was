package com.example.momowas.user.service;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void create(User user){
        userRepository.save(user);
    }

    public UserDto read(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        return UserDto.fromEntity(user);
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
}
