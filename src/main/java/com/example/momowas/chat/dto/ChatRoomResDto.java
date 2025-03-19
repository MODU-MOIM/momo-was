package com.example.momowas.chat.dto;

import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResDto {

    private Long roomId;
    private String name;
    private String crewName;
    private String bannerImage;
    private String leaderNickname;
    private int chatMemberNumbers;
    private LocalDateTime createdAt;

    @Builder
    public ChatRoomResDto(Long roomId, String name, String crewName, String bannerImage, String leaderNickname, int chatMemberNumbers, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.name = name;
        this.crewName = crewName;
        this.bannerImage = bannerImage;
        this.leaderNickname = leaderNickname;
        this.chatMemberNumbers = chatMemberNumbers;
        this.createdAt = createdAt;
    }

    public static ChatRoomResDto fromEntity(ChatRoom chatRoom, User user, Crew crew){
        int memberNumbers = 0;
        if(chatRoom.getChatMembers()!=null){
            memberNumbers = chatRoom.getChatMembers().size();
        }
        return ChatRoomResDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .crewName(crew.getName())
                .bannerImage(crew.getBannerImage())
                .leaderNickname(user.getNickname())
                .chatMemberNumbers(memberNumbers)
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}