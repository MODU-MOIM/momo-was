package com.example.momowas.record.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String thumbnailImage;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_member_id")
    private CrewMember crewMember;

    @Builder
    private Record(String title,
                   String content,
                   String thumbnailImage,
                   Crew crew,
                   CrewMember crewMember) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        } else if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.title=title;
        this.content=content;
        this.thumbnailImage=thumbnailImage;
        this.crew = Objects.requireNonNull(crew, "crew는 null이 될 수 없습니다.");
        this.crewMember = Objects.requireNonNull(crewMember, "crewMember는 null이 될 수 없습니다.");
    }
}
