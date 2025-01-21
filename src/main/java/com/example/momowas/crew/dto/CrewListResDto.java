package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.user.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;

public record CrewListResDto(Long crewId,
                         String name,
                         Category category,
                         String description,
                         Integer minMembers,
                         Integer maxMembers,
                         Integer minAge,
                         Integer maxAge,
                         Gender genderRestriction,
                         String bannerImage,
                         List<RegionDto> regions,
                         LocalDateTime createdAt) {
    public static CrewListResDto of(Crew crew, List<RegionDto> regions) {
        return new CrewListResDto(
                crew.getId(),
                crew.getName(),
                crew.getCategory(),
                crew.getDescription(),
                crew.getMinMembers(),
                crew.getMaxMembers(),
                crew.getMinAge(),
                crew.getMaxAge(),
                crew.getGenderRestriction(),
                crew.getBannerImage(),
                regions,
                crew.getCreatedAt()
        );
    }
}
