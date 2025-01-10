package com.example.momowas.crew.repository;

import com.example.momowas.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByName(String name);
}
