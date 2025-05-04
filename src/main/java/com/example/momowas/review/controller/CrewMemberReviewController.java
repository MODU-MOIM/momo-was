package com.example.momowas.review.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.dto.CrewMemberReviewReqDto;
import com.example.momowas.review.dto.CrewMemberReviewResDto;
import com.example.momowas.review.service.CrewMemberReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/members")
@RequiredArgsConstructor
public class CrewMemberReviewController {
    private final JwtUtil jwtUtil;
    private final CrewMemberReviewService crewMemberReviewService;
    @PostMapping("/{memberId}/reviews")
    public Map<String, Object> createCrewMemberReview(HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long memberId, @RequestBody CrewMemberReviewReqDto crewMemberReviewReqDto){
        Long writerId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        Long crewMemberReviewId = crewMemberReviewService.createCrewMemberReview(crewId, writerId, memberId, crewMemberReviewReqDto);

        return Map.of("reviewId", crewMemberReviewId);
    }

    @PutMapping("/{memberId}/reviews/{reviewId}")
    public CommonResponse<String> updateCrewReview(HttpServletRequest request, @PathVariable Long reviewId, @RequestBody CrewMemberReviewReqDto crewMemberReviewReqDto ){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewMemberReviewService.updateCrewReview(reviewId, userId, crewMemberReviewReqDto);
        return CommonResponse.of(ExceptionCode.SUCCESS,"업데이트 완료");
    }

    @GetMapping("/{memberId}/reviews")
    public List<CrewMemberReviewResDto> getCrewMemberReviewByTargetId(@PathVariable Long crewId, @PathVariable Long memberId){
        return crewMemberReviewService.getCrewMemberReviewByTargetId(crewId, memberId);
    }

    @GetMapping("/{memberId}/reviews/exist")
    public Map<String, Object> isExistCrewMemberReview (HttpServletRequest request, @PathVariable Long crewId, @PathVariable Long memberId){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return Map.of("written",  crewMemberReviewService.isExistCrewMemberReview(crewId, userId, memberId));
    }

    @DeleteMapping("/{memberId}/reviews/{reviewId}")
    public CommonResponse<String> deleteCrewMemberReview(HttpServletRequest request, @PathVariable Long reviewId ){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewMemberReviewService.deleteCrewMemberReview(reviewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,"리뷰 삭제 완료");
    }

    @GetMapping("/{memberId}/reviews/me")
    public List<CrewMemberReviewResDto> getCrewMemberReviewsByTargetIdFromMe (HttpServletRequest request,  @PathVariable Long crewId, @PathVariable Long memberId){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return crewMemberReviewService.getCrewMemberReviewsByTargetIdFromMe(crewId, userId, memberId);
    }
}
