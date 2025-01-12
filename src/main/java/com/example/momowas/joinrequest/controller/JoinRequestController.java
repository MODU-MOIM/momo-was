package com.example.momowas.joinrequest.controller;

import com.example.momowas.joinrequest.dto.JoinRequestListResDto;
import com.example.momowas.joinrequest.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/join-requests")
@RequiredArgsConstructor
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    /* 크루 가입 요청 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> createJoinRequest(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        return Map.of("joinRequestId",joinRequestService.createJoinRequest(crewId, userId));
    }

    /* 크루 가입 요청 목록 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public List<JoinRequestListResDto> getJoinRequestList(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        return joinRequestService.getJoinRequestList(crewId);
    }
}
