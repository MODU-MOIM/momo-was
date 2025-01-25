package com.example.momowas.comment.service;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.comment.dto.CommentReqDto;
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
    private final CommentService commentService;

    /* 피드 댓글 작성 */
    @Transactional
    public Long createFeedComment(CommentReqDto commentReqDto, Long crewId, Long feedId, Long userId) {
        Feed feed = feedService.findFeedById(feedId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment comment = commentRepository.save(commentReqDto.toEntity(feed, crewMember, null));
        return comment.getId();
    }

    /* 피드 댓글 수정 */
    @Transactional
    public void updateFeedComment(CommentReqDto commentReqDto, Long crewId, Long commentId, Long userId) {
        Comment comment = commentService.findCommentById(commentId);
        commentService.validateWriter(crewId, userId, comment);
        comment.updateContent(commentReqDto.content());
    }

    /* 피드 댓글 삭제 */
    @Transactional
    public void deleteFeedComment(Long crewId,Long commentId, Long userId) {
        Comment comment = commentService.findCommentById(commentId);
        commentService.validateWriter(crewId, userId, comment);
        commentRepository.delete(comment);
    }

    /* 피드 대댓글 작성 */
    @Transactional
    public Long replyFeedComment(CommentReqDto commentReqDto, Long crewId, Long parentId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Comment parent = commentService.findCommentById(parentId);
        Comment comment = commentRepository.save(commentReqDto.toEntity(parent.getFeed(), crewMember, parent));
        return comment.getId();
    }

}
