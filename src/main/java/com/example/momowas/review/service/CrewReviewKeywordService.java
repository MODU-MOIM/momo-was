package com.example.momowas.review.service;

import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.CrewReviewKeyword;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.repository.CrewReviewKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewReviewKeywordService {

    private final CrewReviewKeywordRepository crewReviewKeywordRepository;

    /* 크루 평가 id로 크루 평가-키워드 조회 */
    @Transactional(readOnly = true)
    public List<CrewReviewKeyword> findCrewReviewKeywordById(Long crewReviewId) {
        return crewReviewKeywordRepository.findByCrewReviewId(crewReviewId);
    }

    /* 크루 평가-키워드 리스트로 키워드 리스트 추출 */
    public List<Keyword> extractKeywordList(List<CrewReviewKeyword> crewReviewKeywords) {
        return crewReviewKeywords.stream()
                .map(CrewReviewKeyword::getKeyword).toList();
    }

    /* 크루 평가-키워드 생성 */
    @Transactional
    public void createCrewReviewKeyword(List<Keyword> keywords, CrewReview crewReview) {

        for (Keyword keyword : keywords) {

            CrewReviewKeyword crewReviewKeyword = CrewReviewKeyword.builder()
                    .keyword(keyword)
                    .crewReview(crewReview)
                    .build();

            crewReviewKeywordRepository.save(crewReviewKeyword);

        }
    }

    /* 크루 평가-키워드 수정 */
    @Transactional
    public void updateCrewReviewKeyword(List<Keyword> keywords, CrewReview crewReview) {
        List<CrewReviewKeyword> crewReviewKeywords = findCrewReviewKeywordById(crewReview.getId());

        //기존에 키워드가 존재
        if (!crewReviewKeywords.isEmpty()) {

            //요청에 키워드가 존재 -> 수정
            if (!keywords.isEmpty()) {
                crewReviewKeywordRepository.deleteByCrewReviewId(crewReview.getId());
                createCrewReviewKeyword(keywords, crewReview);
            }

            //요청에 키워드가 존재 x -> 삭제
            else {
                crewReviewKeywordRepository.deleteByCrewReviewId(crewReview.getId());
            }
        }

        //기존에 키워드가 존재 x
        else {

            //요청에 키워드가 존재 -> 생성
            if (!keywords.isEmpty()) {
                createCrewReviewKeyword(keywords, crewReview);
            }

            //요청에 키워드가 존재 x -> 아무 것도 하지 않음.

        }
    }
}
