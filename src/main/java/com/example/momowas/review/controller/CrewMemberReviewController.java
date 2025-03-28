package com.example.momowas.review.controller;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.review.dto.CrewMemberReviewReqDto;
import com.example.momowas.review.service.CrewMemberReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        Long crewMemberReviewId = crewMemberReviewService.createCrewMemberReview(crewId, memberId, writerId, crewMemberReviewReqDto);

        return Map.of("reviewId", crewMemberReviewId);
    }
}
