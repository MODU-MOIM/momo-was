package com.example.momowas.review.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.domain.CrewReviewKeyword;
import com.example.momowas.review.domain.Keyword;
import com.example.momowas.review.dto.CrewReviewKeywordCountListResDto;
import com.example.momowas.review.repository.CrewReviewKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CrewReviewKeywordService {

    private final CrewReviewKeywordRepository crewReviewKeywordRepository;
    private final CrewService crewService;

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

    /* 특정 크루의 모든 리뷰의 키워드별 개수 카운트 */
    public List<CrewReviewKeywordCountListResDto> countCrewReviewKeywords(Long crewId) {
        Crew crew = crewService.findCrewById(crewId);

        Map<Keyword, Integer> keywordCount = new HashMap<>();

        crew.getCrewReviews().stream()
                .forEach((crewReview) -> {
                    crewReview.getCrewReviewKeywords().stream()
                            .forEach((crewReviewKeyword) -> {
                                Keyword keyword = crewReviewKeyword.getKeyword();
                                keywordCount.put(keyword, keywordCount.getOrDefault(keyword,0)+1);
                            });
                });

        for (Keyword keyword : keywordCount.keySet()) {
            System.out.println(keyword+": "+keywordCount.get(keyword));

        }

        return keywordCount.keySet().stream()
                .map(keyword -> CrewReviewKeywordCountListResDto.of(keyword, keywordCount.get(keyword)))
                .toList();
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
