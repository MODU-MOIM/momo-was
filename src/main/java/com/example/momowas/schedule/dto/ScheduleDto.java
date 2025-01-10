package com.example.momowas.schedule.dto;

import com.example.momowas.schedule.domain.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.*;

@Data
public class ScheduleDto {
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private String title;
    private String description;
    private Long crewId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isOnline;
    private String detailAddress;

    @Builder
    public ScheduleDto(LocalDate scheduleDate, LocalTime scheduleTime, String title, String description, Long crewId,Long userId, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean isOnline, String detailAddress) {
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.title = title;
        this.description = description;
        this.crewId = crewId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isOnline = isOnline;
        this.detailAddress = detailAddress;
    }
    public static ScheduleDto fromEntity(Schedule schedule){
        return ScheduleDto.builder()
                .scheduleDate(schedule.getScheduleDate())
                .scheduleTime(schedule.getScheduleTime())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .crewId(schedule.getCrewId())
                .userId(schedule.getUserId())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .isOnline(schedule.getIsOnline())
                .detailAddress(schedule.getDetailAddress())
                .build();
    }
}
