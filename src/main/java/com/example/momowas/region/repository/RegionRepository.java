package com.example.momowas.region.repository;

import com.example.momowas.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findByRegionDepth1AndRegionDepth2AndRegionDepth3(String regionDepth1, String regionDepth2, String regionDepth3);

}
