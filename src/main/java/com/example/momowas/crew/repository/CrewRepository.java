package com.example.momowas.crew.repository;

import com.example.momowas.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByName(String name);
    Optional<Crew> findByName(String name);

}
