package com.example.momowas.userInterests.repository;

import com.example.momowas.userInterests.domain.UserInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestsRepository extends JpaRepository<UserInterests, Long> {

}
