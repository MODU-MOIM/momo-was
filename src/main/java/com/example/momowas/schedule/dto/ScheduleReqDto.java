package com.example.momowas.schedule.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleReqDto {
    private LocalDate date;
    private LocalTime time;
    private String title;
    private String description;
    private String detailAddress;
    private Long crewId;
    private Boolean isOnline;
}
