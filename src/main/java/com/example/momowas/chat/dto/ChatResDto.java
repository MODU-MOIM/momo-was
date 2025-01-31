package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.Chat;
import com.example.momowas.crew.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatResDto {
    private Long roomId;
    private Long senderId;
    private String writerName;
    private Role role;
    private String message;
    private LocalDateTime sendAt;

    @Builder
    public ChatResDto(Long roomId, Long senderId, String writerName, Role role, String message, LocalDateTime sendAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.writerName = writerName;
        this.role = role;
        this.message = message;
        this.sendAt = sendAt;
    }

    public static ChatResDto fromEntity(Chat chat, Role role){
        return ChatResDto.builder()
                .roomId(chat.getChatRoom().getId())
                .senderId(chat.getSenderId())
                .writerName(chat.getSenderName())
                .role(role)
                .message(chat.getContent())
                .sendAt(chat.getSendAt())
                .build();
    }
}