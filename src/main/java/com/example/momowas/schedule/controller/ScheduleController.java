package com.example.momowas.schedule.controller;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.SchedulePermissionReqDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.service.ScheduleService;
import com.example.momowas.crewmember.service.CrewMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("isAuthenticated() and @crewManager.hasScheduleCreatePermission(#crewId, #userId)")
    public ScheduleDto createSchedule(@AuthenticationPrincipal Long userId, @PathVariable Long crewId, @RequestBody ScheduleReqDto scheduleReqDto) {
        return scheduleService.createSchedule(userId, crewId, scheduleReqDto);
    }

    @DeleteMapping("/{scheduleId}")
    public CommonResponse<String> deleteSchedule(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long scheduleId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        scheduleService.deleteSchedule(userId, crewId, scheduleId);
        return CommonResponse.of(ExceptionCode.SUCCESS, "일정 삭제 성공");
    }

    @PutMapping("/{scheduleId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasScheduleUpdatePermission(#crewId, #userId)")
    public ScheduleDto updateSchedule(@AuthenticationPrincipal Long userId, @PathVariable Long crewId, @PathVariable Long scheduleId, @RequestBody ScheduleReqDto scheduleReqDto) {
        return scheduleService.updateSchedule(userId, scheduleId, scheduleReqDto);
    }

    @GetMapping("/{scheduleId}")
    public ScheduleDto getByScheduleId(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long scheduleId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByScheduleId(scheduleId);
    }

    @GetMapping("/daily")
    public List<ScheduleDto> getByThisDay(HttpServletRequest request, @PathVariable Long crewId, @RequestParam LocalDate date) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByDate(crewId, date);
    }

    @GetMapping("/monthly")
    public List<ScheduleDto> getByThisMonth(HttpServletRequest request, @PathVariable Long crewId, @RequestParam String yearMonth) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        return scheduleService.getByThisMonth(crewId, yearMonth);
    }

    @PatchMapping("/permissions")
    public CommonResponse<String> updateSchedulePermission(@RequestBody SchedulePermissionReqDto schedulePermissionReqDto, HttpServletRequest request, @PathVariable Long crewId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(crewId, userId);
        scheduleService.updateSchedulePermission(schedulePermissionReqDto, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
