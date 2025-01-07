package com.example.momowas.chat.domain;

import com.example.momowas.chat.dto.MessageType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Chat(Long crewId, Long userId, MessageType type, String content) {
        this.crewId = crewId;
        this.userId = userId;
        this.type = type;
        this.content = content;
    }
}
