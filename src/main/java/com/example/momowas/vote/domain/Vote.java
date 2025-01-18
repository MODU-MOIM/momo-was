package com.example.momowas.vote.domain;

import com.example.momowas.voteparticipant.domain.VoteParticipant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VoteParticipant> voteParticipants = new ArrayList<>();

    @Builder
    public Vote(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.title=title;
    }
}
