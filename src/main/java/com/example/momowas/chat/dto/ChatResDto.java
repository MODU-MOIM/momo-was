package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.Chat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatResDto {
    private Long roomId;
    private Long senderId;
    private String writerName;
    private String message;
    private LocalDateTime sendAt;

    @Builder
    public ChatResDto(Long roomId, Long senderId, String writerName, String message, LocalDateTime sendAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.writerName = writerName;
        this.message = message;
        this.sendAt = sendAt;
    }

    public static ChatResDto fromEntity(Chat chat){
        return ChatResDto.builder()
                .roomId(chat.getChatRoom().getId())
                .senderId(chat.getSenderId())
                .writerName(chat.getSenderName())
                .message(chat.getContent())
                .sendAt(chat.getSendAt())
                .build();
    }
}