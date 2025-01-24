package com.example.momowas.photo.dto;

import com.example.momowas.photo.domain.Photo;

public record PhotoResDto(Long photoId,
                          String url,
                          Integer sequence) {
    public static PhotoResDto of(Photo photo) {
        return new PhotoResDto(
                photo.getId(),
                photo.getUrl(),
                photo.getSequence());
    }
}
