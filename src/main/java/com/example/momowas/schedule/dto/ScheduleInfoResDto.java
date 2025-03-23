package com.example.momowas.schedule.dto;

import com.example.momowas.schedule.domain.Schedule;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScheduleInfoResDto(
        String title,
        LocalDate date,
        String detailAddress
) {
    public static ScheduleInfoResDto fromEntity(Schedule schedule) {
        return ScheduleInfoResDto
                .builder()
                .title(schedule.getTitle())
                .date(schedule.getScheduleDate())
                .detailAddress(schedule.getDetailAddress())
                .build();
    }
}
