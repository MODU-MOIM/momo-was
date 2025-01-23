package com.example.momowas.feed.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.dto.FeedListResDto;
import com.example.momowas.feed.dto.FeedReqDto;
import com.example.momowas.feed.repository.FeedRepository;
import com.example.momowas.feedtag.domain.FeedTag;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.tag.domain.Tag;
import com.example.momowas.tag.service.TagService;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final UserService userService;
    private final TagService tagService;

    /* 피드 id로 피드 조회 */
    @Transactional(readOnly = true)
    public Feed findFeedById(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_FEED));
    }

    /* 피드 생성 */
    @Transactional
    public Long createFeed(FeedReqDto feedReqDto, Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Feed feed = feedRepository.save(feedReqDto.toEntity(crew, crewMember));
        return feed.getId();
    }

    /* 전체 피드 조회 */
    @Transactional(readOnly = true)
    public List<FeedListResDto> getFeedList(Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        User user = userService.findUserById(userId);

        return crew.getFeeds()
                .stream()
                .map(feed -> FeedListResDto.of(feed, user))
                .collect(Collectors.toList());
    }

    /* 피드 삭제 */
    @Transactional
    public void deleteFeed(Long feedId, Long crewId, Long userId) {
        Feed feed = findFeedById(feedId);
        validateWriter(crewId, userId, feed);

        List<Tag> tags = feed.getFeedTags().stream()
                .map(FeedTag::getTag)
                .toList();  //해당 피드의 태그 추출

        feedRepository.delete(feed); //피드 삭제
        tagService.deleteTags(tags); //태그 삭제
    }

    /* 사용자가 피드 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, Feed feed) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!feed.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
