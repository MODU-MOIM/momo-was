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
@RequestMapping("/crews/{crewId}/archives/{archiveId}/comments")
@RequiredArgsConstructor
public class ArchiveCommentController {
    private final CommentService commentService;

    /* 기록 댓글 작성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createArchiveComment(@RequestBody CommentReqDto commentReqDto,
                                                    @PathVariable Long crewId,
                                                    @PathVariable Long archiveId,
                                                    @AuthenticationPrincipal Long userId) {
        Long commentId = commentService.createComment(commentReqDto, crewId, archiveId, null, userId, BoardType.ARCHIVE);
        return Map.of("commentId", commentId);
    }

    /* 기록 댓글 수정 */
    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateArchiveComment(@RequestBody CommentReqDto commentReqDto,
                                                       @PathVariable Long crewId,
                                                       @PathVariable Long commentId,
                                                       @AuthenticationPrincipal Long userId) {
        commentService.updateComment(commentReqDto, crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 기록 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteFeedComment(@PathVariable Long crewId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal Long userId) {
        commentService.deleteComment(crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 기록 대댓글 작성 */
    @PostMapping("/{parentId}/replies")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> replyFeedComment(@RequestBody CommentReqDto commentReqDto,
                                                @PathVariable Long crewId,
                                                @PathVariable Long archiveId,
                                                @PathVariable Long parentId,
                                                @AuthenticationPrincipal Long userId) {
        Long replyId = commentService.createComment(commentReqDto, crewId, archiveId, parentId, userId, BoardType.ARCHIVE);
        return Map.of("replyId", replyId);
    }
}
