package com.example.momowas.review.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.review.domain.CrewMemberReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewMemberReviewRepository extends JpaRepository<CrewMemberReview, Long> {
    List<CrewMemberReview> findByWriterAndTarget(CrewMember writer, CrewMember target);
    List<CrewMemberReview> findByTarget(CrewMember target);
}
