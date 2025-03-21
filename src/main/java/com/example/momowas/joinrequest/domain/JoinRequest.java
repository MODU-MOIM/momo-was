package com.example.momowas.joinrequest.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class JoinRequest {
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
    private RequestStatus requestStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    private JoinRequest(Crew crew, User user, RequestStatus requestStatus) {
        this.crew = Objects.requireNonNull(crew, "crew는 null이 될 수 없습니다.");
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
        this.requestStatus = Objects.requireNonNull(requestStatus, "requestStatus는 null이 될 수 없습니다.");
    }

    public static JoinRequest of(Crew crew, User user, RequestStatus requestStatus) {
        return JoinRequest.builder()
                .crew(crew)
                .user(user)
                .requestStatus(requestStatus)
                .build();
    }

    /* 가입 요청 상태 변경 */
    public void updateRequestStatus(RequestStatus requestStatus) {
        this.requestStatus=requestStatus;
    }
}
