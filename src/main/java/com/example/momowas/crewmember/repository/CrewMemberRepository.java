package com.example.momowas.crewmember.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember,Long> {
    boolean existsByCrewIdAndUserId(Long crewId, Long userId);
    Optional<CrewMember> findByCrewIdAndUserId(Long crewId, Long userId);
}
