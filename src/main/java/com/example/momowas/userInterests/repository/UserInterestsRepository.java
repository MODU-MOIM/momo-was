package com.example.momowas.userInterests.repository;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.user.domain.User;
import com.example.momowas.userInterests.domain.UserInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInterestsRepository extends JpaRepository<UserInterests, Long> {
    List<UserInterests> findByUser(User user);
    void deleteByUser(User user);
}
