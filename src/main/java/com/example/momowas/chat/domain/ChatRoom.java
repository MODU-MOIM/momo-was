package com.example.momowas.chat.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMember> chatMembers = new ArrayList<>();

    @Builder
    public ChatRoom(Long crewId, String name, LocalDateTime createdAt, List<ChatMember> chatMembers) {
        this.crewId = crewId;
        this.name = name;
        this.createdAt = createdAt;
        this.chatMembers = chatMembers;
    }
}
