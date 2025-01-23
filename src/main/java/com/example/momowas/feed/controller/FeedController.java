package com.example.momowas.feed.controller;

import com.example.momowas.feed.dto.FeedDetailResDto;
import com.example.momowas.feed.dto.FeedListResDto;
import com.example.momowas.feed.dto.FeedReqDto;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
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

    /* 피드 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createFeed(@RequestPart FeedReqDto feedReqDto,
                                          @RequestPart(required = false, value = "photos") List<MultipartFile> files,
                                          @PathVariable Long crewId,
                                          @AuthenticationPrincipal Long userId) throws IOException {
        Long feedId = feedService.createFeed(feedReqDto, files, crewId, userId); //피드 저장
        return Map.of("feedId", feedId);
    }

    /* 전체 피드 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public List<FeedListResDto> getFeedList(@PathVariable Long crewId,
                                            @AuthenticationPrincipal Long userId) {
        return feedService.getFeedList(crewId, userId);
    }

    /* 특정 피드 조회 */
    @GetMapping("/{feedId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public FeedDetailResDto getFeedDetail(@PathVariable Long crewId,
                                          @PathVariable Long feedId,
                                          @AuthenticationPrincipal Long userId) {
        return feedService.getFeedDetail(feedId, crewId, userId);
    }

    /* 피드 수정 */
    @PutMapping("/{feedId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateFeed(@RequestPart FeedReqDto feedReqDto,
                                             @RequestPart(required = false, value = "photos") List<MultipartFile> files,
                                             @PathVariable Long feedId,
                                             @PathVariable Long crewId,
                                             @AuthenticationPrincipal Long userId) throws IOException {
        feedService.updateFeed(feedReqDto, files, feedId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 피드 삭제 */
    @DeleteMapping("/{feedId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteFeed(@PathVariable Long feedId,
                                             @PathVariable Long crewId,
                                             @AuthenticationPrincipal Long userId) {
        feedService.deleteFeed(feedId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
