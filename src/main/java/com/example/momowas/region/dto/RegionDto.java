package com.example.momowas.region.dto;

import com.example.momowas.region.domain.Region;

public record RegionDto(String regionDepth1, String regionDepth2, String regionDepth3) {
    public static RegionDto fromEntity(Region region) {
        return new RegionDto(
                region.getRegionDepth1(),
                region.getRegionDepth2(),
                region.getRegionDepth3()
        );
    }
}
