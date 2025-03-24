package com.example.momowas.review.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.schedule.domain.Schedule;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScheduleReviewEventResDto(
    Long crewId,
    String crewName,
    Long scheduleId,
    String scheduleTitle,
    LocalDate scheduleDate
) {
    public static ScheduleReviewEventResDto of(Crew crew, Schedule schedule) {
        return ScheduleReviewEventResDto.builder()
                .crewId(crew.getId())
                .crewName(crew.getName())
                .scheduleId(schedule.getId())
                .scheduleTitle(schedule.getTitle())
                .scheduleDate(schedule.getScheduleDate())
                .build();
    }
}
