package com.example.momowas.like.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.like.domain.Like;
import com.example.momowas.like.repository.LikeRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final LikeRepository likeRepository;
    private final CrewMemberService crewMemberService;
    private final FeedService feedService;

    /* 사용자의 좋아요 여부 확인 */
    @Transactional
    public boolean hasLikedFeed(Feed feed, CrewMember crewMember) {
        return likeRepository.existsByFeedAndCrewMember(feed, crewMember);
    }

    /* 피드 좋아요 */
    @Transactional
    public void likeFeed(Long feedId, Long crewId, Long userId) {
        Feed feed = feedService.findFeedById(feedId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if (hasLikedFeed(feed, crewMember)) {
            throw new BusinessException(ExceptionCode.ALREADY_LIKE_FEED);
        }
        likeRepository.save(Like.of(feed, crewMember));
    }

    /* 피드 좋아요 취소 */
    @Transactional
    public void unlikeFeed(Long feedId, Long crewId, Long userId) {
        Feed feed = feedService.findFeedById(feedId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Like like = likeRepository.findByFeedAndCrewMember(feed, crewMember)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_FEED_LIKE));
        likeRepository.delete(like);
    }
}
