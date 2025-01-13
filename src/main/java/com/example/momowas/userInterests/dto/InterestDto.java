package com.example.momowas.userInterests.dto;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.userInterests.domain.UserInterests;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class InterestDto {
    private List<Category> interests;

    @Builder
    public InterestDto(List<Category> interests) {
        this.interests = interests;
    }

}
