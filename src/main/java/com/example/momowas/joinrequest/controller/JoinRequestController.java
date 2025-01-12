package com.example.momowas.joinrequest.controller;

import com.example.momowas.joinrequest.dto.JoinRequestListResDto;
import com.example.momowas.joinrequest.dto.JoinRequestReqDto;
import com.example.momowas.joinrequest.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    /* 크루 가입 요청 */
    @PostMapping("/join-requests")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> createJoinRequest(@RequestBody JoinRequestReqDto joinRequestReqDto, @AuthenticationPrincipal Long userId) {
        return Map.of("joinRequestId",joinRequestService.createJoinRequest(joinRequestReqDto, userId));
    }

    /* 크루 가입 요청 목록 조회 */
    @GetMapping("/{crewId}/join-requests")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public List<JoinRequestListResDto> getJoinRequestList(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        return joinRequestService.getJoinRequestList(crewId);
    }

    /* 크루 가입 요청 수락 */
    @PostMapping("/join-requests/{joinRequestId}/accept")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public Map<String, Object> acceptJoinRequest(@PathVariable Long joinRequestId, @AuthenticationPrincipal Long userId) {
        return Map.of("crewMemberId",joinRequestService.acceptJoinRequest(joinRequestId));
    }

    /* 크루 가입 요청 거절 */
    @PostMapping("/join-requests/{joinRequestId}/reject")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public void rejectJoinRequest(@PathVariable Long joinRequestId, @AuthenticationPrincipal Long userId) {
        joinRequestService.rejectJoinRequest(joinRequestId);
    }

}
