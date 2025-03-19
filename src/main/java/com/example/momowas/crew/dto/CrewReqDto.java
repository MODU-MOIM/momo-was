package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.user.domain.Gender;

import java.util.List;

public record CrewReqDto(
        String name,
        Category category,
        String description,
        Integer maxMembers,
        Integer minAge,
        Integer maxAge,
        Gender genderRestriction,
        List<RegionDto> regions
){
    public Crew toEntity(String bannerImageUrl) {
        return Crew.builder()
                .name(name)
                .category(category)
                .description(description)
                .maxMembers(maxMembers)
                .minAge(minAge)
                .maxAge(maxAge)
                .genderRestriction(genderRestriction)
                .bannerImage(bannerImageUrl)
                .build();
    }
}
