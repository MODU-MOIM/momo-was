package com.example.momowas.recommend.dto;

import com.example.momowas.crew.domain.Category;
import lombok.Builder;
import lombok.Data;

@Data
public class HotPlaceResDto {
    private String detailAddress;
    private Category category;

    @Builder
    public HotPlaceResDto(String detailAddress, Category category) {
        this.detailAddress = detailAddress;
        this.category = category;
    }
}
