package com.example.momowas.voteparticipant.controller;

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

    /* 투표 참여자 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String,Object> createVoteParticipant(@RequestBody VoteParticipantReqDto voteParticipantReqDto, @PathVariable Long crewId, @PathVariable Long voteId, @AuthenticationPrincipal Long userId) {
        VoteParticipant voteParticipant = voteParticipantService.createVoteParticipant(voteParticipantReqDto, crewId, voteId, userId);
        return Map.of("voteParticipantId", voteParticipant.getId());
    }

}
