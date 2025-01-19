package com.example.momowas.voteparticipant.controller;

import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.example.momowas.voteparticipant.dto.VoteParticipantReqDto;
import com.example.momowas.voteparticipant.service.VoteParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crews/{crewId}/notices/{noticeId}/vote/{voteId}")
public class VoteParticipantController {

    private final VoteParticipantService voteParticipantService;

    /* 특정 공지의 투표 참여 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String,Object> createVoteParticipant(@RequestBody VoteParticipantReqDto voteParticipantReqDto, @PathVariable Long crewId, @PathVariable Long voteId, @AuthenticationPrincipal Long userId) {
        VoteParticipant voteParticipant = voteParticipantService.createVoteParticipant(voteParticipantReqDto, crewId, voteId, userId);
        return Map.of("voteParticipantId", voteParticipant.getId());
    }

    /* 특정 공지의 투표 재참여 */
    @PutMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateVoteParticipant(@RequestBody VoteParticipantReqDto voteParticipantReqDto, @PathVariable Long crewId, @PathVariable Long voteId, @AuthenticationPrincipal Long userId) {
        voteParticipantService.updateVoteParticipant(voteParticipantReqDto, crewId, voteId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
