package com.example.momowas.chat.dto;

import lombok.Data;

@Data
public class ChatReqDto {
    private Long senderId;
    private String content;
}
