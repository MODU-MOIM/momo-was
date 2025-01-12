package com.example.momowas.joinrequest.repository;

import com.example.momowas.joinrequest.domain.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest,Long> {
    boolean existsByCrewIdAndUserId(Long crewId, Long userId);
    List<JoinRequest> findByCrewId(Long crewId);
}
