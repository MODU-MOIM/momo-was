package com.example.momowas.like.controller;

import com.example.momowas.like.service.FeedLikeService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crews/{crewId}/feeds/{feedId}/likes")
@RequiredArgsConstructor
public class FeedLikeController {
    private final FeedLikeService feedLikeService;

    /* 피드 좋아요 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> likeFeed(@PathVariable Long crewId,
                                           @PathVariable Long feedId,
                                           @AuthenticationPrincipal Long userId) {
        feedLikeService.likeFeed(feedId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 좋아요 취소 */
    @DeleteMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> unlikeFeed(@PathVariable Long crewId,
                                             @PathVariable Long feedId,
                                             @AuthenticationPrincipal Long userId) {
        feedLikeService.unlikeFeed(feedId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
