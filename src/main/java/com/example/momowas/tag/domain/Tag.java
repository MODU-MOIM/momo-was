package com.example.momowas.tag.domain;

import com.example.momowas.feedtag.domain.FeedTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FeedTag> feedTags = new ArrayList<>();

    @Builder
    private Tag(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("namet은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.name=name;
    }

    public static Tag of(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }
}

