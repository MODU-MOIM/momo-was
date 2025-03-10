package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.Chat;
import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatResDto {
    private Long roomId;
    private Long senderId;
    private String writerName;
    private String profileImage;
    private Role role;
    private String message;
    private LocalDateTime sendAt;

    @Builder
    public ChatResDto(Long roomId, Long senderId, String writerName, String profileImage, Role role, String message, LocalDateTime sendAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.writerName = writerName;
        this.profileImage = profileImage;
        this.role = role;
        this.message = message;
        this.sendAt = sendAt;
    }

    public static ChatResDto fromEntity(Chat chat, User user, Role role){

        return ChatResDto.builder()
                .roomId(chat.getChatRoom().getId())
                .senderId(chat.getSenderId())
                .writerName(chat.getSenderName())
                .profileImage(user.getProfileImage())
                .role(role)
                .message(chat.getContent())
                .sendAt(chat.getSendAt())
                .build();
    }
}