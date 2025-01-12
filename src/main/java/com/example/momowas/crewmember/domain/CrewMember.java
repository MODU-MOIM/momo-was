package com.example.momowas.crewmember.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.domain.Role;
import com.example.momowas.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CrewMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime joinedAt;

    @Builder
    private CrewMember(Crew crew, User user, Role role, LocalDateTime joinedAt) {
        this.crew= Objects.requireNonNull(crew,"crew는 null이 될 수 없습니다.");
        this.user=Objects.requireNonNull(user,"user는 null이 될 수 없습니다.");
        this.role=Objects.requireNonNull(role,"role는 null이 될 수 없습니다.");
        this.joinedAt=Objects.requireNonNull(joinedAt, "createdAt는 null이 될 수 없습니다.");
    }

    public static CrewMember of(Crew crew, User user, Role role) {
        return CrewMember.builder()
                .crew(crew)
                .user(user)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}
