package com.example.momowas.voteparticipant.service;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteDetailResDto;
import com.example.momowas.vote.service.VoteService;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import com.example.momowas.voteparticipant.dto.VoteParticipantReqDto;
import com.example.momowas.voteparticipant.repository.VoteParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteParticipantService {

    private final VoteParticipantRepository voteParticipantRepository;
    private final VoteService voteService;
    private final CrewMemberService crewMemberService;

    /* 투표 id와 크루 멤버 id로 투표 참여자 조회 */
    @Transactional(readOnly = true)
    public VoteParticipant findVoteParticipantByVoteAndCrewMember(Long voteId, Long crewMemberId) {
        return voteParticipantRepository.findByVoteIdAndCrewMemberId(voteId, crewMemberId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_VOTE_PARTICIPANT));
    }

    /* 투표 id와 크루 멤버 id로 투표 참여자 존재 유무 확인 */
    @Transactional(readOnly = true)
    public boolean isVoteParticipantExists(Long voteId, Long crewMemberId) {
        return voteParticipantRepository.existsByVoteIdAndCrewMemberId(voteId, crewMemberId);
    }

    /* 특정 공지의 투표 참여 */
    @Transactional()
    public VoteParticipant createVoteParticipant(VoteParticipantReqDto voteParticipantReqDto, Long crewId, Long voteId, Long userId) {
        Vote vote = voteService.findVoteById(voteId);
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        //이미 참여한 투표인지 검증
        if (isVoteParticipantExists(voteId, crewMember.getId())) {
            throw new BusinessException(ExceptionCode.ALREADY_PARTICIPATE_VOTE);
        }
        return voteParticipantRepository.save(voteParticipantReqDto.toEntity(vote, crewMember));
    }

    /* 특정 공지의 투표 재참여 */
    @Transactional()
    public void updateVoteParticipant(VoteParticipantReqDto voteParticipantReqDto, Long crewId, Long voteId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        VoteParticipant voteParticipant = findVoteParticipantByVoteAndCrewMember(voteId, crewMember.getId());
        voteParticipant.revote(voteParticipantReqDto.voteStatus());
    }

    /* 크루 멤버의 투표 상태 조회 */
    @Transactional(readOnly = true)
    public VoteDetailResDto getVoteDetail(Vote vote, CrewMember crewMember) {
        if (vote == null) {
            return VoteDetailResDto.of(false,null,null);
        }
        VoteParticipant voteParticipant = voteParticipantRepository.findByVoteIdAndCrewMemberId(vote.getId(), crewMember.getId()).orElse(null);
        return VoteDetailResDto.of(true, vote, voteParticipant);
    }
}
