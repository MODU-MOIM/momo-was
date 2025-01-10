package com.example.momowas.crewregion.domain;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.region.domain.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @Builder
    private CrewRegion(Region region, Crew crew) {
        this.region= Objects.requireNonNull(region,"region는 null이 될 수 없습니다.");
        this.crew=Objects.requireNonNull(crew,"crew는 null이 될 수 없습니다.");
    }

    public static CrewRegion of(Region region, Crew crew) {
        return CrewRegion.builder()
                .region(region)
                .crew(crew)
                .build();
    }
}
