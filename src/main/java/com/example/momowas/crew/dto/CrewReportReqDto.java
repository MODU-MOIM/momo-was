package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.region.dto.RegionDto;

import java.util.List;

public record CrewReportReqDto(
        List<RegionDto> regions,
        Category category,
        String description
        ) {
}
