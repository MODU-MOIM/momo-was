package com.example.momowas.notice.controller;

import com.example.momowas.notice.domain.Notice;
import com.example.momowas.notice.domain.NoticeType;
import com.example.momowas.notice.dto.NoticeDetailResDto;
import com.example.momowas.notice.dto.NoticeListResDto;
import com.example.momowas.notice.dto.NoticeReqDto;
import com.example.momowas.notice.service.NoticeService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final VoteService voteService;

    /* 공지 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public Map<String,Object> createNotice(@RequestBody NoticeReqDto noticeReqDto, @PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        Long noticeId = noticeService.createGeneralNotice(noticeReqDto, crewId, userId);
        return Map.of("noticeId", noticeId);
    }

    /* 전체 공지 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public List<NoticeListResDto> getNoticeList(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        return noticeService.getNoticeList(crewId);
    }

    /* 특정 공지 조회 */
    @GetMapping("/{noticeId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public NoticeDetailResDto getNoticeDetail(@PathVariable Long crewId, @PathVariable Long noticeId, @AuthenticationPrincipal Long userId) {
        return noticeService.getNoticeDetail(crewId, noticeId, userId);
    }

    /* 공지 수정 */
    @PutMapping("/{noticeId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateNotice(@RequestBody NoticeReqDto noticeReqDto, @PathVariable Long crewId, @PathVariable Long noticeId, @AuthenticationPrincipal Long userId) {
        noticeService.updateNotice(noticeReqDto, crewId, noticeId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 공지 삭제 */
    @DeleteMapping("/{noticeId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> deleteNotice(@PathVariable Long crewId, @PathVariable Long noticeId, @AuthenticationPrincipal Long userId) {
        noticeService.deleteNotice(noticeId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 공지 상단 고정 또는 고정 되있으면 해제*/
    @PatchMapping("/{noticeId}/pin-toggle")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public Map<String,Object> togglePinNotice(@PathVariable Long crewId, @PathVariable Long noticeId, @AuthenticationPrincipal Long userId) {
        Long pinnedNoticeId = noticeService.togglePinNotice(noticeId);
        return Map.of("noticeId", pinnedNoticeId);
    }

}
