package com.example.momowas.schedule.service;

import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.repository.ScheduleRepository;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;

    public ScheduleDto createSchedule(Long userId, ScheduleReqDto scheduleReqDto){
        UserDto userDto  = userService.read(userId);

        //크루 검증 추가

        Schedule schedule = Schedule.builder()
                .scheduleDate(scheduleReqDto.getDate())
                .scheduleTime(scheduleReqDto.getTime())
                .title(scheduleReqDto.getTitle())
                .description(scheduleReqDto.getDescription())
                .crewId(scheduleReqDto.getCrewId())
                .userId(userDto.getId())
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .isOnline(scheduleReqDto.isOnline())
                .detailAddress(scheduleReqDto.getDetailAddress())
                .build();

        scheduleRepository.save(schedule);
        return ScheduleDto.fromEntity(schedule);
    }


}
