package com.example.momowas.archive.dto;

import com.example.momowas.archive.domain.Archive;

import java.time.LocalDateTime;

public record ArchiveListResDto(Long archiveId,
                                String thumbnailImage,
                                String title,
                                LocalDateTime createdAt,
                                int commentCount
                                ) {
    public static ArchiveListResDto of(Archive archive) {
        return new ArchiveListResDto(
                archive.getId(),
                archive.getThumbnailImage(),
                archive.getTitle(),
                archive.getCreatedAt(),
                archive.getComments().size()
        );
    }
}
