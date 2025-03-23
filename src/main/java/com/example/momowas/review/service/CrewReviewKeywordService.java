package com.example.momowas.review.service;

import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.CrewReviewKeyword;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.repository.CrewReviewKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewReviewKeywordService {

    private final CrewReviewKeywordRepository crewReviewKeywordRepository;

    /* 크루 리뷰-키워드 생성 */
    @Transactional
    public void createCrewReviewKeyword(Keyword keyword, CrewReview crewReview) {
        CrewReviewKeyword crewReviewKeyword = CrewReviewKeyword.builder()
                .keyword(keyword)
                .crewReview(crewReview)
                .build();

        crewReviewKeywordRepository.save(crewReviewKeyword);
    }
}
