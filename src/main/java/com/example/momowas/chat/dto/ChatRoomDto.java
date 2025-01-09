package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoomDto {

    private Long roomId;
    private String name;
    private LocalDateTime createdAt;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(Long roomId, String name, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static ChatRoomDto fromEntity(ChatRoom chatRoom){
        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}