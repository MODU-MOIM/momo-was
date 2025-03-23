package com.example.momowas.review.repository;

import com.example.momowas.review.domain.CrewReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewReviewRepository extends JpaRepository<CrewReview, Long> {
}
