package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.user.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;

public record CreateCrewReqDto(
        String name,
        Category category,
        String description,
        String descriptionImage,
        Integer minMembers,
        Integer maxMembers,
        Integer minAge,
        Integer maxAge,
        Gender genderRestriction,
        String bannerImage,
        List<RegionDto> regions
){
    public Crew toEntity() {
        return Crew.builder()
                .name(name)
                .category(category)
                .description(description)
                .descriptionImage(descriptionImage)
                .minMembers(minMembers)
                .maxMembers(maxMembers)
                .minAge(minAge)
                .maxAge(maxAge)
                .genderRestriction(genderRestriction)
                .bannerImage(bannerImage)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
