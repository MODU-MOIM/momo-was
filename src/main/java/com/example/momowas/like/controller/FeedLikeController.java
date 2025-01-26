package com.example.momowas.like.controller;

import com.example.momowas.comment.domain.BoardType;
import com.example.momowas.like.service.LikeService;
import com.example.momowas.recommend.service.RecommendService;
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
    private final LikeService likeService;
    private final RecommendService recommendService;
    /* 피드 좋아요 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> likeFeed(@PathVariable Long crewId,
                                           @PathVariable Long feedId,
                                           @AuthenticationPrincipal Long userId) {
        likeService.likeBoard(feedId, crewId, userId, BoardType.FEED);
        //추천 로직
        recommendService.handleFeedEvent(feedId, "like");
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 좋아요 취소 */
    @DeleteMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> unlikeFeed(@PathVariable Long crewId,
                                             @PathVariable Long feedId,
                                             @AuthenticationPrincipal Long userId) {
        likeService.unlikeBoard(feedId, crewId, userId, BoardType.FEED);
        //추천 로직
        recommendService.handleFeedEvent(feedId, "unLike");
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
