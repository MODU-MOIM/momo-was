package com.example.momowas.review.repository;

import com.example.momowas.review.domain.CrewReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewReviewKeywordRepository extends JpaRepository<CrewReviewKeyword, Long> {
}
