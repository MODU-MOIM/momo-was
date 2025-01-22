package com.example.momowas.feed.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.dto.FeedReqDto;
import com.example.momowas.feed.repository.FeedRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;

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
}
