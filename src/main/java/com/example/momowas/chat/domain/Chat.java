package com.example.momowas.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private MessageType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendAt;

    @Builder
    public Chat(ChatRoom chatRoom, Long senderId, String senderName,  MessageType type, String content, LocalDateTime sendAt) {
        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.senderName = senderName;
        this.type = type;
        this.content = content;
        this.sendAt = sendAt;
    }
}
