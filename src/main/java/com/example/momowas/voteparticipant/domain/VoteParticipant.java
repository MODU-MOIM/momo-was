package com.example.momowas.voteparticipant.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VoteParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Builder
    public VoteParticipant(AttendanceStatus status) {
        this.status = Objects.requireNonNull(status, "status는 null이 될 수 없습니다.");
    }
}
