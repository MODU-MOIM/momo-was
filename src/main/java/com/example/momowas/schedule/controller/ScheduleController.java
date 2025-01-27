package com.example.momowas.schedule.controller;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.service.ScheduleService;
import com.example.momowas.crewmember.service.CrewMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/crews/{crewId}/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JwtUtil jwtUtil;
    private final CrewManager crewManager;


    @PostMapping("")
    private ScheduleDto createSchedule(HttpServletRequest request, @PathVariable Long crewId, @RequestBody ScheduleReqDto scheduleReqDto) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.createSchedule(userId, crewId, scheduleReqDto);
    }

    @DeleteMapping("/{scheduleId}")
    private CommonResponse<String> deleteSchedule(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long scheduleId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        scheduleService.deleteSchedule(userId, crewId, scheduleId);
        return CommonResponse.of(ExceptionCode.SUCCESS, "일정 삭제 성공");
    }

    @PutMapping("/{scheduleId}")
    private ScheduleDto updateSchedule(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long scheduleId, @RequestBody ScheduleReqDto scheduleReqDto) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.updateSchedule(userId, scheduleId, scheduleReqDto);
    }

    @GetMapping("/{scheduleId}")
    private ScheduleDto getByScheduleId(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long scheduleId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByScheduleId(scheduleId);
    }

    @GetMapping("/daily")
    private List<ScheduleDto> getByThisDay(HttpServletRequest request, @PathVariable Long crewId, @RequestParam LocalDate date) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByDate(crewId, date);
    }

    @GetMapping("/monthly")
    private List<ScheduleDto> getByThisMonth(HttpServletRequest request, @PathVariable Long crewId, @RequestParam String yearMonth) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByThisMonth(crewId, yearMonth);
    }
}
