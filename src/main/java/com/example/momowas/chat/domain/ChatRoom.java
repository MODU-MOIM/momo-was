package com.example.momowas.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    @Column(nullable = false)
    private Long crewId;

    @Column
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ChatRoom(Long crewId, String name, LocalDateTime createdAt) {
        this.crewId = crewId;
        this.name = name;
        this.createdAt = createdAt;
    }
}
