package com.example.momowas.feed.controller;

import com.example.momowas.feed.dto.FeedListResDto;
import com.example.momowas.feed.dto.FeedReqDto;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.feedtag.repository.FeedTagRepository;
import com.example.momowas.feedtag.service.FeedTagService;
import com.example.momowas.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/feeds")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    private final PhotoService photoService;
    private final FeedTagService feedTagService;

    /* 피드 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createFeed(@RequestPart FeedReqDto feedReqDto,
                                        @RequestPart(required = false, value = "photos") List<MultipartFile> files,
                                        @PathVariable Long crewId,
                                        @AuthenticationPrincipal Long userId) throws IOException {
        Long feedId = feedService.createFeed(feedReqDto, crewId, userId); //피드 저장
        photoService.createPhoto(files, feedId); //사진 저장
        feedTagService.createFeedTag(feedReqDto.tagNames(), feedId); //태그 저장
        return Map.of("feedId", feedId);
    }

    /* 전체 피드 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public List<FeedListResDto> getFeedList(@PathVariable Long crewId,
                                            @AuthenticationPrincipal Long userId) {
        return feedService.getFeedList(crewId, userId);
    }
}
