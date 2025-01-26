package com.example.momowas.schedule.repository;

import com.example.momowas.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByCrewIdAndScheduleDate(Long crewId, LocalDate date);
    List<Schedule> findByCrewIdAndScheduleDateBetween(Long crewId, LocalDate startDate, LocalDate endDate);
    List<Schedule> findByScheduleDate(LocalDate scheduleDate);
}
