package com.example.momowas.vote.service;

import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteReqDto;
import com.example.momowas.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    /* 투표 id로 투표 조회 */
    @Transactional(readOnly = true)
    public Vote findVoteById(Long voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_VOTE));
    }

    /* 투표 생성 */
    @Transactional
    public Vote createVote(VoteReqDto voteReqDto) {
        return voteRepository.save(voteReqDto.toEntity());
    }

}
