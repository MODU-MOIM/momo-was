package com.example.momowas.crewmember.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember,Long> {
}
