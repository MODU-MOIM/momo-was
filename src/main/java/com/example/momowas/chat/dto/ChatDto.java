package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatDto {
    private Long roomId;
    private Long senderId;
    private String writerName;
    private String message;
    private LocalDateTime sendAt;

    @Builder
    public ChatDto(Long roomId, Long senderId,  String writerName, String message, LocalDateTime sendAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.writerName = writerName;
        this.message = message;
        this.sendAt = sendAt;
    }

    public static ChatDto fromEntity(Chat chat){
        return ChatDto.builder()
                .roomId(chat.getChatRoom().getId())
                .senderId(chat.getSenderId())
                .writerName(chat.getSenderName())
                .message(chat.getContent())
                .sendAt(chat.getSendAt())
                .build();
    }
}