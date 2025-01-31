package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResDto {

    private Long roomId;
    private String name;
    private int chatMemberNumbers;
    private LocalDateTime createdAt;

    @Builder
    public ChatRoomResDto(Long roomId, String name, int chatMemberNumbers, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.name = name;
        this. chatMemberNumbers = chatMemberNumbers;
        this.createdAt = createdAt;
    }

    public static ChatRoomResDto fromEntity(ChatRoom chatRoom){
        return ChatRoomResDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .chatMemberNumbers(chatRoom.getChatMembers().size())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}