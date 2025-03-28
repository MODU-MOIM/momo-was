package com.example.momowas.review.repository;

import com.example.momowas.review.domain.CrewMemberReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberReviewRepository extends JpaRepository<CrewMemberReview, Long> {
}
