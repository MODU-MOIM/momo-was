package com.example.momowas.recommend.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ArchiveRecommendDto {
    private Long archiveId;
    private Long crewId;

    @Builder
    public ArchiveRecommendDto(Long archiveId, Long crewId) {
        this.archiveId = archiveId;
        this.crewId = crewId;
    }
}
