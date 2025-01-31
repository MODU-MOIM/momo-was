package com.example.momowas.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FindEmailResDto {
    String email;

    @Builder
    public FindEmailResDto(String email) {
        this.email = email;
    }
}
