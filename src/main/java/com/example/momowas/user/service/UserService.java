package com.example.momowas.user.service;

import com.example.momowas.user.domain.User;
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
}
