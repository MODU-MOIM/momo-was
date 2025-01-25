package com.example.momowas.comment.controller;

import com.example.momowas.comment.domain.BoardType;
import com.example.momowas.comment.dto.CommentReqDto;
import com.example.momowas.comment.service.CommentService;
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

    private final CommentService commentService;

    /* 피드 댓글 작성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createFeedComment(@RequestBody CommentReqDto commentReqDto,
                                                 @PathVariable Long crewId,
                                                 @PathVariable Long feedId,
                                                 @AuthenticationPrincipal Long userId) {
        Long commentId = commentService.createComment(commentReqDto, crewId, feedId, null, userId, BoardType.FEED);
        return Map.of("commentId", commentId);
    }

    /* 피드 댓글 수정 */
    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateFeedComment(@RequestBody CommentReqDto commentReqDto,
                                                    @PathVariable Long crewId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal Long userId) {
        commentService.updateComment(commentReqDto, crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteFeedComment(@PathVariable Long crewId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal Long userId) {
        commentService.deleteComment(crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 대댓글 작성 */
    @PostMapping("/{parentId}/replies")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> replyFeedComment(@RequestBody CommentReqDto commentReqDto,
                                                @PathVariable Long crewId,
                                                @PathVariable Long feedId,
                                                @PathVariable Long parentId,
                                                @AuthenticationPrincipal Long userId) {
        Long replyId = commentService.createComment(commentReqDto, crewId, feedId, parentId, userId, BoardType.FEED);
        return Map.of("replyId", replyId);
    }

}
