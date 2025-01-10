package com.example.momowas.schedule.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.schedule.dto.ScheduleDto;
import com.example.momowas.schedule.dto.ScheduleReqDto;
import com.example.momowas.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
