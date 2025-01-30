package com.example.momowas.crewmember.controller;

import com.example.momowas.crewmember.dto.CrewMemberListResDto;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crews/{crewId}/members")
@RequiredArgsConstructor
public class CrewMemberController {
    private final CrewMemberService crewMemberService;

    /* 전체 크루 멤버 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public List<CrewMemberListResDto> getCrewMemberList(@PathVariable Long crewId,
                                                        @AuthenticationPrincipal Long userId) {
        return crewMemberService.getCrewMemberList(crewId);
    }

    /* 특정 크루 멤버 강제 탈퇴 */
    @DeleteMapping("/{memberId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> removeCrewMember(@PathVariable Long crewId,
                                                   @PathVariable Long memberId,
                                                   @AuthenticationPrincipal Long userId) {
        crewMemberService.removeCrewMember(memberId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
