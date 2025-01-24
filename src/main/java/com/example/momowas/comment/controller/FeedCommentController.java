package com.example.momowas.comment.controller;

import com.example.momowas.comment.dto.FeedCommentReqDto;
import com.example.momowas.comment.service.FeedCommentService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crews/{crewId}/feeds/{feedId}/comments")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;

    /* 피드 댓글 작성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createFeedComment(@RequestBody FeedCommentReqDto feedCommentReqDto,
                                                 @PathVariable Long crewId,
                                                 @PathVariable Long feedId,
                                                 @AuthenticationPrincipal Long userId) {
        Long commentId = feedCommentService.createFeedComment(feedCommentReqDto, crewId, feedId, userId);
        return Map.of("commentId", commentId);
    }

    /* 피드 댓글 수정 */
    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateFeedComment(@RequestBody FeedCommentReqDto feedCommentReqDto,
                                                    @PathVariable Long crewId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal Long userId) {
        feedCommentService.updateFeedComment(feedCommentReqDto, crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteFeedComment(@PathVariable Long crewId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal Long userId) {
        feedCommentService.deleteFeedComment(crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 대댓글 작성 */
    @PostMapping("/{parentId}/replies")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> replyFeedComment(@RequestBody FeedCommentReqDto feedCommentReqDto,
                                                   @PathVariable Long crewId,
                                                   @PathVariable Long parentId,
                                                   @AuthenticationPrincipal Long userId) {
        Long replyId = feedCommentService.replyFeedComment(feedCommentReqDto, crewId, parentId, userId);
        return Map.of("replyId", replyId);
    }

}
