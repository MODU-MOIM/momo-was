package com.example.momowas.crew.domain;

import com.example.momowas.crew.dto.CrewReqDto;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.region.dto.RegionDto;
import com.example.momowas.user.domain.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private String descriptionImage;

    private Integer minMembers;

    private Integer maxMembers;

    private Integer minAge; //'제한없음'은 null로 처리.

    private Integer maxAge; //'제한없음'은 null로 처리.

    @Enumerated(EnumType.STRING)
    private Gender genderRestriction; //'제한없음'은 null로 처리.

    private String bannerImage;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "crew", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CrewMember> crewMembers = new ArrayList<>();

    @OneToMany(mappedBy = "crew", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CrewRegion> crewRegions = new ArrayList<>();

    @Builder
    private Crew(String name,
                 Category category,
                 String description,
                 String descriptionImage,
                 Integer minMembers,
                 Integer maxMembers,
                 Integer minAge,
                 Integer maxAge,
                 Gender genderRestriction,
                 String bannerImage,
                 LocalDateTime createdAt
    ) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        } else if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("description은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.name = name;
        this.category = Objects.requireNonNull(category, "category는 null이 될 수 없습니다.");
        this.description = description;
        this.descriptionImage = descriptionImage;
        this.minMembers = Objects.requireNonNull(minMembers, "minMembers는 null이 될 수 없습니다.");
        this.maxMembers = Objects.requireNonNull(maxMembers, "maxMembers는 null이 될 수 없습니다.");
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.genderRestriction = genderRestriction;
        this.bannerImage = bannerImage;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt는 null이 될 수 없습니다.");
    }


    public void update(String name,
                       Category category,
                       String description,
                       String descriptionImage,
                       Integer minMembers,
                       Integer maxMembers,
                       Integer minAge,
                       Integer maxAge,
                       Gender genderRestriction,
                       String bannerImage
    ) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        } else if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("description은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.name = name;
        this.category = Objects.requireNonNull(category, "category는 null이 될 수 없습니다.");
        this.description = description;
        this.descriptionImage = descriptionImage;
        this.minMembers = Objects.requireNonNull(minMembers, "minMembers는 null이 될 수 없습니다.");
        this.maxMembers = Objects.requireNonNull(maxMembers, "maxMembers는 null이 될 수 없습니다.");
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.genderRestriction = genderRestriction;
        this.bannerImage = bannerImage;
    }

    /* 크루 정원이 초과했는지 */
    public boolean isCrewFull() {
        return Hibernate.size(crewRegions)>=maxMembers;
    }
}
