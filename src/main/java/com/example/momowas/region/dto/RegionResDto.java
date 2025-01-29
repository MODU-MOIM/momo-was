package com.example.momowas.region.dto;

import com.example.momowas.region.domain.Region;

public record RegionResDto(String regionDepth1, String regionDepth2) {
    public static RegionResDto fromEntity(Region region) {
        return new RegionResDto(
                region.getRegionDepth1(),
                region.getRegionDepth2()
        );
    }
}
