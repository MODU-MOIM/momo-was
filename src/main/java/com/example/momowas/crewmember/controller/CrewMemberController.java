package com.example.momowas.crewmember.controller;

import com.example.momowas.crewmember.dto.CrewMemberListResDto;
import com.example.momowas.crewmember.dto.CrewMemberRoleReqDto;
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
    @PreAuthorize("isAuthenticated()")
    public List<CrewMemberListResDto> getCrewMemberList(@PathVariable Long crewId){
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

    /* 크루 멤버 권한 설정 */
    @PatchMapping("/{memberId}/role")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrewMemberRole(@RequestBody CrewMemberRoleReqDto crewMemberRoleReqDto,
                                                       @PathVariable Long crewId,
                                                       @PathVariable Long memberId,
                                                       @AuthenticationPrincipal Long userId) {
        crewMemberService.updateCrewMemberRole(crewMemberRoleReqDto, memberId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 크루 리더 위임 */
    @PatchMapping("/{memberId}/leader")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> delegateCrewLeader(@PathVariable Long crewId,
                                                     @PathVariable Long memberId,
                                                     @AuthenticationPrincipal Long userId) {
        crewMemberService.delegateCrewLeader(memberId, crewId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

}
