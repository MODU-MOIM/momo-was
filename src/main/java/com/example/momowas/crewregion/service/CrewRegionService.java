package com.example.momowas.crewregion.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.crewregion.repository.CrewRegionRepository;
import com.example.momowas.region.domain.Region;
import com.example.momowas.region.dto.RegionResDto;
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
    public List<CrewRegion> createCrewRegion(List<RegionResDto> regionResDtos, Crew crew) {

        List<CrewRegion> crewRegions = regionResDtos.stream()
                .map(regionDto -> {
                    Region region = regionService.findRegion(regionDto.regionDepth1(), regionDto.regionDepth2());
                    return CrewRegion.of(region, crew);
                }).collect(Collectors.toList());

        return crewRegionRepository.saveAll(crewRegions);
    }

    /* 특정 크루의 지역 조회 */
    public List<RegionResDto> findRegionByCrewId(Long CrewId) {
        return crewRegionRepository.findByCrewId(CrewId).stream()
                .map(crewRegion -> {
                    Region region = regionService.findRegionByRegionId(crewRegion.getRegion().getId());
                    return RegionResDto.fromEntity(region);
                })
                .collect(Collectors.toList());
    }

    /* 특정 크루의 지역 수정 */
    public void updateCrewRegion(List<CrewRegion> crewRegions, List<RegionResDto> regionResDtos, Crew crew) {
        crewRegionRepository.deleteAll(crewRegions); //기존 crewRegion 다 지움
        createCrewRegion(regionResDtos, crew); //새로 추가
    }

}
