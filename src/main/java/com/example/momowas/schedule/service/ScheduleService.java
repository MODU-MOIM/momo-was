package com.example.momowas.schedule.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.SchedulePermissionReqDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.repository.ScheduleRepository;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final CrewService crewService;
    private final RecommendService recommendService;
    private final CrewManager crewManager;

    public ScheduleDto createSchedule(Long userId, Long crewId, ScheduleReqDto scheduleReqDto){
        if(!crewManager.hasScheduleCreatePermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        User user  = userService.findUserById(userId);
        Crew crew = crewService.findCrewById(crewId);

        Schedule schedule = Schedule.builder()
                .scheduleDate(scheduleReqDto.getDate())
                .scheduleTime(scheduleReqDto.getTime())
                .title(scheduleReqDto.getTitle())
                .description(scheduleReqDto.getDescription())
                .crewId(crew.getId())
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .isOnline(scheduleReqDto.getIsOnline())
                .detailAddress(scheduleReqDto.getDetailAddress())
                .build();

        scheduleRepository.save(schedule);

        //추천
        recommendService.handleCrewEvent(crewId, "addSchedule", crew.getCrewMembers().size(), crew.getMaxMembers());
        return ScheduleDto.fromEntity(schedule);
    }

    @Transactional
    public void deleteSchedule(Long userId, Long crewId, Long scheduleId){
        if(!crewManager.hasCrewPermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new BusinessException(ExceptionCode.SCHEDULE_NOT_FOUND));
        User user  = userService.findUserById(userId);
        Crew crew = crewService.findCrewById(crewId);

        recommendService.handleCrewEvent(crewId, "deleteSchedule", crew.getCrewMembers().size(), crew.getMaxMembers());
        scheduleRepository.delete(schedule);
    }

    @Transactional
    public ScheduleDto updateSchedule(Long userId, Long crewId, Long scheduleId, ScheduleReqDto scheduleReqDto){
        if(!crewManager.hasScheduleUpdatePermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new BusinessException(ExceptionCode.SCHEDULE_NOT_FOUND));
        User user  = userService.findUserById(userId);

        schedule.updateSchedule(
                scheduleReqDto.getDate() == null? schedule.getScheduleDate():scheduleReqDto.getDate(),
                scheduleReqDto.getTime()==null? schedule.getScheduleTime() : scheduleReqDto.getTime(),
                scheduleReqDto.getTitle()==null ? schedule.getTitle() : scheduleReqDto.getTitle(),
                scheduleReqDto.getDescription(),
                scheduleReqDto.getDetailAddress(),
                scheduleReqDto.getIsOnline() == null ? schedule.getIsOnline() : scheduleReqDto.getIsOnline()
        );

        return ScheduleDto.fromEntity(schedule);
    }

    public ScheduleDto getByScheduleId(Long userId, Long crewId, Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new BusinessException(ExceptionCode.SCHEDULE_NOT_FOUND));
        if(!crewManager.hasCrewPermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
        return ScheduleDto.fromEntity(schedule);
    }
    public Schedule getScheduleByScheduleId(Long scheduleId){
        return scheduleRepository.findById(scheduleId).orElseThrow(()->new BusinessException(ExceptionCode.SCHEDULE_NOT_FOUND));
    }

    public List<ScheduleDto> getByDate(Long userId, Long crewId, LocalDate date){
        if(!crewManager.hasCrewPermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
        return scheduleRepository.findByCrewIdAndScheduleDate(crewId, date).stream()
                .map(ScheduleDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ScheduleDto> getByThisMonth(Long userId, Long crewId, String yearMonth) {
        if(!crewManager.hasCrewPermission(crewId, userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
        YearMonth yearMonthObj = YearMonth.parse(yearMonth); // "yyyy-MM" 형식이어야 함
        LocalDate startDate = yearMonthObj.atDay(1);
        LocalDate endDate = yearMonthObj.atEndOfMonth();

        return scheduleRepository.findByCrewIdAndScheduleDateBetween(crewId, startDate, endDate).stream()
                .map(ScheduleDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedulesByDate(LocalDate date){
        return scheduleRepository.findByScheduleDate(date);
    }

    @Transactional
    public void updateSchedulePermission(SchedulePermissionReqDto schedulePermissionReqDto, Long crewId) {
        Crew crew = crewService.findCrewById(crewId);
        crew.updateScheduleCreatePermission(schedulePermissionReqDto.createPermission());
        crew.updateScheduleUpdatePermission(schedulePermissionReqDto.updatePermission());
    }
}
