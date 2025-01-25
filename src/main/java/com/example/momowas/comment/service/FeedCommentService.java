package com.example.momowas.comment.service;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.comment.dto.FeedCommentReqDto;
import com.example.momowas.comment.repository.CommentRepository;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final CommentRepository commentRepository;;
    private final CrewMemberService crewMemberService;
    private final FeedService feedService;

    /* 피드 댓글 조회 */
    @Transactional(readOnly = true)
    public Comment findFeedCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_COMMENT));
    }

    /* 피드 댓글 작성 */
    @Transactional
    public Long createFeedComment(FeedCommentReqDto feedCommentReqDto, Long crewId, Long feedId, Long userId) {
        Feed feed = feedService.findFeedById(feedId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment comment = commentRepository.save(feedCommentReqDto.toEntity(feed, crewMember, null));
        return comment.getId();
    }

    /* 피드 댓글 수정 */
    @Transactional
    public void updateFeedComment(FeedCommentReqDto feedCommentReqDto, Long crewId, Long commentId, Long userId) {
        Comment comment = findFeedCommentById(commentId);
        validateWriter(crewId, userId, comment);
        comment.updateContent(feedCommentReqDto.content());
    }

    /* 피드 댓글 삭제 */
    @Transactional
    public void deleteFeedComment(Long crewId,Long commentId, Long userId) {
        Comment comment = findFeedCommentById(commentId);
        validateWriter(crewId, userId, comment);
        commentRepository.delete(comment);
    }

    /* 피드 대댓글 작성 */
    @Transactional
    public Long replyFeedComment(FeedCommentReqDto feedCommentReqDto, Long crewId, Long parentId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment parent = findFeedCommentById(parentId);
        Comment comment = commentRepository.save(feedCommentReqDto.toEntity(parent.getFeed(), crewMember, parent));
        return comment.getId();
    }

    /* 사용자가 피드 댓글 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, Comment comment) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!comment.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
