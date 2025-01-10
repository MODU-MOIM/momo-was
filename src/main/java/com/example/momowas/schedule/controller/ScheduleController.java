package com.example.momowas.schedule.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final JwtUtil jwtUtil;
    private final ScheduleService scheduleService;

    @PostMapping("")
    private ScheduleDto createSchedule(HttpServletRequest request, @RequestBody ScheduleReqDto scheduleReqDto){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return scheduleService.createSchedule(userId, scheduleReqDto);
    }

    @DeleteMapping("/{scheduleId}")
    private CommonResponse<String> deleteSchedule(HttpServletRequest request, @PathVariable Long scheduleId){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        scheduleService.deleteSchedule(userId, scheduleId);
        return CommonResponse.of(ExceptionCode.SUCCESS, "일정 삭제 성공");
    }

    @PutMapping("/{scheduleId}")
    private ScheduleDto updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @RequestBody ScheduleReqDto scheduleReqDto){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return scheduleService.updateSchedule(userId, scheduleId, scheduleReqDto);
    }

}
