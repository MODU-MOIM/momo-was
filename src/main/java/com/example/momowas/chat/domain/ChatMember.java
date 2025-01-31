package com.example.momowas.chat.domain;

import com.example.momowas.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatRoom_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Builder
    public ChatMember(User user, ChatRoom chatRoom, LocalDateTime joinedAt) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.joinedAt = joinedAt;
    }
}
