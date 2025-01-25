package com.example.momowas.notification.service;

import com.example.momowas.notification.domain.Notification;
import com.example.momowas.notification.repository.NotificationRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.domain.Schedule;
import com.example.momowas.schedule.service.ScheduleService;
import com.example.momowas.sms.SmsUtil;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SmsUtil smsUtil;
    private final ScheduleService scheduleService;
    private final UserService userService;

    private void create(Long scheduleId) {
        Schedule schedule = scheduleService.getByScheduleId(scheduleId);

        // 이미 알림이 생성된 스케줄인지 확인
        boolean isAlreadyNotified = notificationRepository.existsByScheduleId(scheduleId);
        if (isAlreadyNotified) {
            return;
        }

        User user = userService.findUserById(schedule.getUserId());
        smsUtil.sendScheduleNotification(user.getCp(), schedule.getTitle());

        Notification notification = Notification.builder()
                .scheduleId(schedule.getId())
                .message(schedule.getTitle() + " 일정이 하루 남았습니다!")
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Scheduled(cron = "0 0 10 * * ?")
    private void sendDailyScheduleNotifications() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Schedule> schedules = scheduleService.getSchedulesByDate(tomorrow);

        for (Schedule schedule : schedules) {
            try {
                this.create(schedule.getId());
            } catch (Exception e) {
                throw new BusinessException(ExceptionCode.SMS_SEND_FAILED);
            }
        }
    }
}
