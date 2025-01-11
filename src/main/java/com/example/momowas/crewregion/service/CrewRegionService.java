package com.example.momowas.crewregion.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.crewregion.repository.CrewRegionRepository;
import com.example.momowas.region.domain.Region;
import com.example.momowas.region.dto.RegionReqDto;
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
    public void createCrewRegion(List<RegionReqDto> regionReqDtos, Crew crew) {

        List<CrewRegion> crewRegions = regionReqDtos.stream()
                .map(regionReqDto -> {
                    Region region = regionService.findRegion(regionReqDto.regionDepth1(), regionReqDto.regionDepth2(), regionReqDto.regionDepth3());
                    return CrewRegion.of(region, crew);
                }).collect(Collectors.toList());

        crewRegionRepository.saveAll(crewRegions);
    }


}
