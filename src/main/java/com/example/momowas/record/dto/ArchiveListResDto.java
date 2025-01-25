package com.example.momowas.record.dto;

import com.example.momowas.record.domain.Archive;

import java.time.LocalDateTime;

public record ArchiveListResDto(String thumbnailImage,
                                String title,
                                LocalDateTime createdAt
                                //댓글 수 추가
                                ) {
    public static ArchiveListResDto of(Archive archive) {
        return new ArchiveListResDto(
                archive.getThumbnailImage(),
                archive.getTitle(),
                archive.getCreatedAt()
        );
    }
}
