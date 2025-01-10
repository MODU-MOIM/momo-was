package com.example.momowas.crewregion.repository;

import com.example.momowas.crewregion.domain.CrewRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRegionRepository extends JpaRepository<CrewRegion, Long> {
}
