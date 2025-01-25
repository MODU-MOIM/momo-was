package com.example.momowas.comment.controller;

import com.example.momowas.comment.dto.CommentReqDto;
import com.example.momowas.comment.service.ArchiveCommentService;
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
    private final ArchiveCommentService archiveCommentService;

    /* 기록 댓글 작성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createArchiveComment(@RequestBody CommentReqDto commentReqDto,
                                                 @PathVariable Long crewId,
                                                 @PathVariable Long archiveId,
                                                 @AuthenticationPrincipal Long userId) {
        Long commentId = archiveCommentService.createArchiveComment(commentReqDto, crewId, archiveId, userId);
        return Map.of("commentId", commentId);
    }

    /* 기록 댓글 수정 */
    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateArchiveComment(@RequestBody CommentReqDto commentReqDto,
                                                       @PathVariable Long crewId,
                                                       @PathVariable Long commentId,
                                                       @AuthenticationPrincipal Long userId) {
        archiveCommentService.updateArchiveComment(commentReqDto, crewId, commentId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

}
