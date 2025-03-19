package com.example.momowas.userInterests.dto;

import com.example.momowas.crew.domain.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategorySelectDto {
    private List<Category> categories;

}
