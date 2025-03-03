package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.user.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;

public record CrewDetailResDto(Long crewId,
                               String name,
                               Category category,
                               Integer memberCount,
                               String description,
                               Integer minMembers,
                               Integer maxMembers,
                               Integer minAge,
                               Integer maxAge,
                               Gender genderRestriction,
                               String bannerImage,
                               List<RegionDto> regions,
                               LocalDateTime createdAt){
    public static CrewDetailResDto of(Crew crew, List<RegionDto> regions, Integer memberCount) {
        return new CrewDetailResDto(
                crew.getId(),
                crew.getName(),
                crew.getCategory(),
                memberCount,
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
