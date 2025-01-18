package com.example.momowas.voteparticipant.service;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.service.VoteService;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.example.momowas.voteparticipant.dto.VoteParticipantReqDto;
import com.example.momowas.voteparticipant.repository.VoteParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteParticipantService {

    private final VoteParticipantRepository voteParticipantRepository;
    private final VoteService voteService;
    private final CrewMemberService crewMemberService;

    public VoteParticipant createVoteParticipant(VoteParticipantReqDto voteParticipantReqDto, Long crewId, Long voteId, Long userId) {
        Vote vote = voteService.findVoteById(voteId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        return voteParticipantRepository.save(voteParticipantReqDto.toEntity(vote, crewMember));
    }

}
