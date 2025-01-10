package com.example.momowas.crew.dto;

import com.example.momowas.crew.domain.Crew;

import java.time.LocalDateTime;

public record CrewResDto(Long crewId, String name, LocalDateTime createdAt) {
    public static CrewResDto fromEntity(Crew crew) {
        return new CrewResDto(crew.getId(), crew.getName(), crew.getCreatedAt());
    }
}
