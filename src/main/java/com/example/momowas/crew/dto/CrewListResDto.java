package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionResDto;
import com.example.momowas.user.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;

public record CrewListResDto(Long crewId,
                         String name,
                         Category category,
                         Integer minMembers,
                         Integer maxMembers,
                         Integer minAge,
                         Integer maxAge,
                         Gender genderRestriction,
                         String bannerImage,
                         List<RegionResDto> regions,
                         LocalDateTime createdAt) {
    public static CrewListResDto of(Crew crew, List<RegionResDto> regions) {
        return new CrewListResDto(
                crew.getId(),
                crew.getName(),
                crew.getCategory(),
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
