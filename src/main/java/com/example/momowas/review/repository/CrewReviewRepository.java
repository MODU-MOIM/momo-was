package com.example.momowas.review.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewReviewRepository extends JpaRepository<CrewReview, Long> {
    List<CrewReview> findByCrewId(Long crewId);

    boolean existsByCrewMemberAndSchedule(CrewMember crewMember, Schedule schedule);
}
