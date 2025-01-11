package com.example.momowas.crewregion.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.crewregion.repository.CrewRegionRepository;
import com.example.momowas.region.domain.Region;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewRegionService {
    private final CrewRegionRepository crewRegionRepository;
    private final RegionService regionService;

    /* 크루-지역 생성 */
    public void createCrewRegion(List<RegionDto> regionDtos, Crew crew) {

        List<CrewRegion> crewRegions = regionDtos.stream()
                .map(regionDto -> {
                    Region region = regionService.findRegion(regionDto.regionDepth1(), regionDto.regionDepth2(), regionDto.regionDepth3());
                    return CrewRegion.of(region, crew);
                }).collect(Collectors.toList());

        crewRegionRepository.saveAll(crewRegions);
    }

    /* 특정 크루의 지역 조회 */
    public List<RegionDto> findRegionByCrewId(Long CrewId) {
        return crewRegionRepository.findByCrewId(CrewId).stream()
                .map(crewRegion -> {
                    Region region = regionService.findRegionByRegionId(crewRegion.getRegion().getId());
                    return RegionDto.fromEntity(region);
                })
                .collect(Collectors.toList());
    }

}
