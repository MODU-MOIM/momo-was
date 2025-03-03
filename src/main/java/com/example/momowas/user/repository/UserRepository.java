package com.example.momowas.user.repository;

import com.example.momowas.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    boolean  existsByNickname(String nickName);
    User findByProviderId(String providerId);

    Optional<User> findByCp(String cp);
}
