package com.example.momowas.like.service;

import com.example.momowas.comment.domain.BoardType;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.like.domain.Like;
import com.example.momowas.like.repository.LikeRepository;
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
public class LikeService {
    private final LikeRepository likeRepository;
    private final CrewMemberService crewMemberService;
    private final FeedService feedService;
    private final ArchiveService archiveService;
    private final RecommendService recommendService;

    /* 게시글 좋아요 */
    @Transactional
    public void likeBoard(Long boardId, Long crewId, Long userId, BoardType boardType) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Like like=null;

        if (boardType.equals(BoardType.FEED)) {
            Feed feed = feedService.findFeedById(boardId);
            if (likeRepository.existsByFeedAndCrewMember(feed, crewMember)) {
                throw new BusinessException(ExceptionCode.ALREADY_LIKE_BOARD);

            }else{
                like=Like.of(feed, crewMember);
            }
        }
        if (boardType.equals(BoardType.ARCHIVE)) {
            Archive archive = archiveService.findArchiveById(boardId);
            if (likeRepository.existsByArchiveAndCrewMember(archive, crewMember)) {
                throw new BusinessException(ExceptionCode.ALREADY_LIKE_BOARD);
            }else{
                like=Like.of(archive, crewMember);
                //추천 로직
                recommendService.handleArchiveEvent(archive.getId(), "like");
            }
        }
        likeRepository.save(like);
    }

    /* 게시글 좋아요 취소 */
    @Transactional
    public void unlikeBoard(Long boardId, Long crewId, Long userId, BoardType boardType) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        Like like=null;
        if (boardType.equals(BoardType.FEED)) {
            Feed feed = feedService.findFeedById(boardId);
            like = likeRepository.findByFeedAndCrewMember(feed, crewMember)
                    .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_LIKE));

        }
        if (boardType.equals(BoardType.ARCHIVE)) {
            Archive archive = archiveService.findArchiveById(boardId);
            like = likeRepository.findByArchiveAndCrewMember(archive, crewMember)
                    .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_LIKE));

            //추천 로직
            recommendService.handleArchiveEvent(archive.getId(), "unLike");
        }

        likeRepository.delete(like);
    }
}
