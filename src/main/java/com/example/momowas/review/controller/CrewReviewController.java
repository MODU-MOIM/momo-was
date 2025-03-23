package com.example.momowas.review.controller;

import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.dto.CrewReviewListResDto;
import com.example.momowas.review.dto.CrewReviewReqDto;
import com.example.momowas.review.service.CrewReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/reviews")
@RequiredArgsConstructor
public class CrewReviewController {
    private final CrewReviewService crewReviewService;

    /* 크루 평가 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createCrewReview(@RequestBody CrewReviewReqDto crewReviewReqDto,
                                                @PathVariable Long crewId,
                                                @AuthenticationPrincipal Long userId) {
        Long crewReviewId = crewReviewService.createCrewReview(crewReviewReqDto, crewId, userId);
        return Map.of("reviewId", crewReviewId);
    }

    /* 전체 크루 평가 조회 */
    @GetMapping("")
    public List<CrewReviewListResDto> getCrewReviewList(@PathVariable Long crewId) {
        return crewReviewService.getCrewReviewList(crewId);
    }

    /* 크루 평가 수정 */
    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateCrewReview(@RequestBody CrewReviewReqDto crewReviewReqDto,
                                                   @PathVariable Long reviewId,
                                                   @PathVariable Long crewId,
                                                   @AuthenticationPrincipal Long userId) {
        crewReviewService.updateCrewReview(crewReviewReqDto, reviewId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 크루 평가 삭제 */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteCrewReview(@PathVariable Long reviewId,
                                                   @PathVariable Long crewId,
                                                   @AuthenticationPrincipal Long userId) {
        crewReviewService.deleteCrewReview(reviewId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
