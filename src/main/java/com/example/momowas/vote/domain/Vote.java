package com.example.momowas.vote.domain;

import com.example.momowas.notice.domain.Notice;
import com.example.momowas.voteparticipant.domain.VoteStatus;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    private String title;

    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VoteParticipant> voteParticipants = new ArrayList<>();

    @OneToOne(mappedBy = "vote", fetch = FetchType.LAZY)
    private Notice notice;

    @Builder
    public Vote(VoteType voteType, String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.voteType= Objects.requireNonNull(voteType,"voteType는 null이 될 수 없습니다.");
        this.title=title;
    }

    /*  투표 참여자의 수 */
    public Long countParticipants() {
        return (long) Hibernate.size(voteParticipants);
    }

    /*  투표 상태가 POSITIVE(참석 또는 찬성)인 투표 참여자의 수 */
    public Long countPositiveParticipants() {
        return voteParticipants.stream().filter(voteParticipant
                -> voteParticipant.getStatus()== VoteStatus.POSITIVE)
                .count();
    }

    /*  투표 상태가 NEGATIVE(미참석 또는 반대)인 투표 참여자의 수 */
    public Long countNegativeParticipants() {
        return voteParticipants.stream().filter(voteParticipant
                        -> voteParticipant.getStatus()== VoteStatus.NEGATIVE)
                .count();
    }

    public void update(VoteType voteType, String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.voteType= Objects.requireNonNull(voteType,"voteType는 null이 될 수 없습니다.");
        this.title=title;
    }

}
