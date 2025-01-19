package com.example.momowas.voteparticipant.domain;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.vote.domain.Vote;
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

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "crew_member_id")
    private CrewMember crewMember;

    @Builder
    public VoteParticipant(AttendanceStatus status, Vote vote, CrewMember crewMember) {
        this.status = Objects.requireNonNull(status, "status는 null이 될 수 없습니다.");
        this.vote = Objects.requireNonNull(vote, "vote는 null이 될 수 없습니다.");
        this.crewMember = Objects.requireNonNull(crewMember, "crewMember는 null이 될 수 없습니다.");
    }

    /* 재투표 */
    public void revote(AttendanceStatus status) {
        this.status = Objects.requireNonNull(status, "status는 null이 될 수 없습니다.");
    }
}
