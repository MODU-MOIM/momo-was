package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.dto.RegionResDto;
import com.example.momowas.user.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;

public record CrewDetailResDto(Long crewId,
                               String name,
                               Category category,
                               String description,
                               Integer minMembers,
                               Integer maxMembers,
                               Integer minAge,
                               Integer maxAge,
                               Gender genderRestriction,
                               String bannerImage,
                               List<RegionResDto> regions,
                               LocalDateTime createdAt){
    public static CrewDetailResDto of(Crew crew, List<RegionResDto> regions) {
        return new CrewDetailResDto(
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
