package com.example.momowas.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private String message;

    @Column
    private boolean isRead;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(Long scheduleId, String message, boolean isRead, LocalDateTime createdAt) {
        this.scheduleId = scheduleId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
