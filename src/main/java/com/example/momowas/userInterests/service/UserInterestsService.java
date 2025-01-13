package com.example.momowas.userInterests.service;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import com.example.momowas.userInterests.domain.UserInterests;
import com.example.momowas.userInterests.dto.CategorySelectDto;
import com.example.momowas.userInterests.repository.UserInterestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInterestsService {

    private final UserInterestsRepository userInterestsRepository;
    private final UserService userService;

    //관심사 생성
    public void createInterests (Long userId, CategorySelectDto categorySelectDto){
        User user = userService.findUserById(userId);

        List<Category> categories= categorySelectDto.getCategories();

        if (!categories.isEmpty()) {
            categories.forEach(category -> {
                UserInterests userInterests = UserInterests.builder()
                        .user(user)
                        .category(category)
                        .build();
                userInterestsRepository.save(userInterests);
            });
        }
    }

}
