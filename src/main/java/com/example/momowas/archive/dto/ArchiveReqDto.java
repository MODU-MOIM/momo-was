package com.example.momowas.archive.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.archive.domain.Archive;

public record ArchiveReqDto(String title,
                            String content,
                            String thumbnailImageUrl

) {
    public Archive toEntity(Crew crew, CrewMember crewMember) {
        return Archive.builder()
                .title(title)
                .content(content)
                .thumbnailImage(thumbnailImageUrl)
                .crew(crew)
                .crewMember(crewMember)
                .build();
    }
}
