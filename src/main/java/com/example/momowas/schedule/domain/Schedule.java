package com.example.momowas.schedule.domain;

import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private LocalTime scheduleTime;

    @Column(nullable = false)
    String title;

    @Column
    String description;

    @Column
    private Long crewId;

    @Column(nullable = false)
    private Long userId;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    boolean isOnline;

    @Column
    String detailAddress;

    @Builder
    public Schedule(LocalDate scheduleDate, LocalTime scheduleTime, String title, String description, Long crewId, Long userId, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean isOnline, String detailAddress) {
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.title = title;
        this.description = description;
        this.crewId = crewId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isOnline = isOnline;
        this.detailAddress = detailAddress;
    }
}
