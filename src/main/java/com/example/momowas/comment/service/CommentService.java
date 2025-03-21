package com.example.momowas.comment.service;

import com.example.momowas.comment.domain.BoardType;
import com.example.momowas.comment.domain.Comment;
import com.example.momowas.comment.dto.CommentReqDto;
import com.example.momowas.comment.repository.CommentRepository;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.archive.domain.Archive;
import com.example.momowas.archive.service.ArchiveService;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CrewMemberService crewMemberService;
    private final FeedService feedService;
    private final ArchiveService archiveService;
    private final RecommendService recommendService;

    /* 댓글 id로 댓글 조회 */
    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_COMMENT));
    }

    /* 댓글 작성 */
    @Transactional
    public Long createComment(CommentReqDto commentReqDto, Long crewId, Long boardId, Long parentId, Long userId, BoardType boardType) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment parent = parentId!=null ? findCommentById(parentId) : null;

        Comment comment=null;
        if (boardType.equals(BoardType.FEED)) {
            Feed feed = feedService.findFeedById(boardId);
            comment=commentReqDto.toEntity(feed, crewMember, parent);
        }

        if (boardType.equals(BoardType.ARCHIVE)) {
            Archive archive = archiveService.findArchiveById(boardId);
            comment = commentRepository.save(commentReqDto.toEntity(archive, crewMember, parent));

            //추천 로직
            recommendService.handleArchiveEvent(comment.getFeed().getId(), "comment");
        }
        return commentRepository.save(comment).getId();
    }

    /* 댓글 수정 */
    @Transactional
    public void updateComment(CommentReqDto commentReqDto, Long crewId, Long commentId, Long userId) {
        Comment comment = findCommentById(commentId);
        validateWriter(crewId, userId, comment);
        comment.updateContent(commentReqDto.content());
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteComment(Long crewId,Long commentId, Long userId, BoardType boardType) {
        Comment comment = findCommentById(commentId);
        validateWriter(crewId, userId, comment);
        commentRepository.delete(comment);

        if (boardType.equals(BoardType.ARCHIVE)) {
            //추천 로직
            recommendService.handleArchiveEvent(comment.getFeed().getId(), "deleteComment");
        }
    }

    /* 사용자가 댓글 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, Comment comment) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!comment.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
