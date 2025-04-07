package com.example.momowas.sse.service;

import com.example.momowas.sse.repository.SseEmitterRepository;
import com.example.momowas.review.dto.ScheduleReviewEventResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final SseEmitterRepository sseEmitterRepository;

    /* 클라이언트의 이벤트 구독을 수락  */
    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitterRepository.save(userId, sseEmitter);

        //SSE 연결이 정상적으로 종료됐거나, 유효 시간 내 이벤트가 전송되지 않아 타임아웃 된 경우
        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(userId));
        sseEmitter.onTimeout(() -> sseEmitterRepository.deleteById(userId));

        //첫 구독 시 이벤트 발생
        sendToClient("sse",userId, "first subscribe");

        return sseEmitter;
    }

    /* 이벤트를 구독한 클라이언트에게 데이터 전송 */
    public void broadcast(String eventName, Long userId, ScheduleReviewEventResDto scheduleReviewEventResDto) {
        sendToClient(eventName, userId, scheduleReviewEventResDto);
    }

    public void sendToClient(String eventName, Long userId, Object data) {
        SseEmitter sseEmitter = sseEmitterRepository.findById(userId);
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .id(userId.toString())
                            .name(eventName)
                            .data(data)
            );
        } catch (IOException e) {
            sseEmitterRepository.deleteById(userId);
            throw new RuntimeException(e);
        }
    }
}
