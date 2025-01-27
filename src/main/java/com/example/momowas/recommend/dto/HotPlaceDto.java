package com.example.momowas.recommend.dto;

import com.example.momowas.crew.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class HotPlaceDto {
    private String detailAddress;
    private Category category;

    @Builder
    public HotPlaceDto(String detailAddress, Category category) {
        this.detailAddress = detailAddress;
        this.category = category;
    }
}
