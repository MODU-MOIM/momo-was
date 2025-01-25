package com.example.momowas.notification.repository;

import com.example.momowas.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    boolean existsByScheduleId(Long scheduleId);
}
