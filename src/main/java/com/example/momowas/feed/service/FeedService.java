package com.example.momowas.feed.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.dto.FeedDetailResDto;
import com.example.momowas.feed.dto.FeedListResDto;
import com.example.momowas.feed.dto.FeedReqDto;
import com.example.momowas.feed.repository.FeedRepository;
import com.example.momowas.feedtag.domain.FeedTag;
import com.example.momowas.feedtag.service.FeedTagService;
import com.example.momowas.like.repository.LikeRepository;
import com.example.momowas.photo.service.PhotoService;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.tag.domain.Tag;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final PhotoService photoService;
    private final FeedTagService feedTagService;
    private final LikeRepository likeRepository; //순환참조 때문에 일단 이렇게 ㅠㅠ 추후 수정 필요
    private final RecommendService recommendService;

    /* 피드 id로 피드 조회 */
    @Transactional(readOnly = true)
    public Feed findFeedById(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_FEED));
    }

    /* 피드 생성 */
    @Transactional
    public Long createFeed(FeedReqDto feedReqDto, List<MultipartFile> files, Long crewId, Long userId) throws IOException, NoSuchAlgorithmException {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        Feed feed = feedRepository.save(feedReqDto.toEntity(crew, crewMember));//피드 저장
        if(files!=null && !files.isEmpty()){
            photoService.createPhoto(files, feed); //사진 저장
        }

        feedTagService.createFeedTag(feedReqDto.tagNames(), feed); //태그 저장
        recommendService.handleCrewEvent(crewId, "addFeed", crew.getCrewMembers().size(), crew.getMaxMembers());
        return feed.getId();
    }

    /* 전체 피드 조회 */
    @Transactional(readOnly = true)
    public List<FeedListResDto> getFeedList(Long crewId, Long userId) {
        Crew crew = crewService.findCrewById(crewId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        return crew.getFeeds()
                .stream()
                .map(feed -> FeedListResDto.of(
                        feed,
                        feed.getCrewMember().getUser(),
                        likeRepository.existsByFeedAndCrewMember(feed, crewMember)))
                .collect(Collectors.toList());
    }

    /* 특정 피드 조회 */
    @Transactional(readOnly = true)
    public FeedDetailResDto getFeedDetail(Long feedId, Long crewId, Long userId) {
        Feed feed = findFeedById(feedId);
        User writer = feed.getCrewMember().getUser();
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        boolean isLiked = likeRepository.existsByFeedAndCrewMember(feed, crewMember);
        return FeedDetailResDto.of(feed, writer, isLiked);
    }

    /* 피드 수정 */
    @Transactional
    public void updateFeed(FeedReqDto feedReqDto, List<MultipartFile> files, Long feedId, Long crewId, Long userId) throws IOException, NoSuchAlgorithmException {
        Feed feed = findFeedById(feedId);
        validateWriter(crewId, userId, feed);

        feed.updateContent(feedReqDto.content()); //피드 내용 수정
        photoService.updatePhoto(files, feed); //사진 수정
        feedTagService.updateTags(feedReqDto.tagNames(), feed); //태그 수정
    }

    /* 피드 삭제 */
    @Transactional
    public void deleteFeed(Long feedId, Long crewId, Long userId) {
        Feed feed = findFeedById(feedId);
        Crew crew = crewService.findCrewById(crewId);
        validateWriter(crewId, userId, feed);

        List<Tag> tags = feed.getFeedTags().stream()
                .map(FeedTag::getTag)
                .toList();  //해당 피드의 태그 추출

        feedRepository.delete(feed); //피드 삭제
        feedTagService.deleteTags(tags); //태그 삭제

        recommendService.handleCrewEvent(crewId, "deleteFeed", crew.getCrewMembers().size(), crew.getMaxMembers());
    }

    /* 사용자가 피드 작성자인지 검증 */
    private void validateWriter(Long crewId, Long userId, Feed feed) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        if(!feed.isWriter(crewMember)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
