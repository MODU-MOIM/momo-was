package com.example.momowas.crew.repository;

import com.example.momowas.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long>, JpaSpecificationExecutor<Crew> {
    boolean existsByName(String name);
    List<Crew> findByCrewMembersUserId(Long userId);

}
