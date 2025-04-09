package com.example.momowas.sse.controller;

import com.example.momowas.sse.service.SseEmitterService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {
    private final SseEmitterService sseEmitterService;

    /* 클라이언트의 이벤트 구독을 수락 */
    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal Long userId, HttpServletResponse response) {
        // SSE 관련 헤더 설정
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("X-Accel-Buffering", "no");

        return sseEmitterService.subscribe(userId);
    }
}
