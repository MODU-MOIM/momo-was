package com.example.momowas.comment.service;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.comment.dto.CommentReqDto;
import com.example.momowas.comment.repository.CommentRepository;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.record.domain.Archive;
import com.example.momowas.record.service.ArchiveService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveCommentService {
    private final CommentRepository commentRepository;
    private final ArchiveService archiveService;
    private final CrewMemberService crewMemberService;
    private final CommentService commentService;

    /* 기록 댓글 작성 */
    @Transactional
    public Long createArchiveComment(CommentReqDto commentReqDto,Long crewId, Long archiveId, Long userId) {
        Archive archive = archiveService.findArchiveById(archiveId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment comment = commentRepository.save(commentReqDto.toEntity(archive, crewMember, null));
        return comment.getId();
    }

    /* 기록 댓글 수정 */
    @Transactional
    public void updateArchiveComment(CommentReqDto commentReqDto,Long crewId, Long commentId, Long userId) {
        Comment comment = commentService.findCommentById(commentId);
        commentService.validateWriter(crewId, userId, comment);
        comment.updateContent(commentReqDto.content());
    }

}
