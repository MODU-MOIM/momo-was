package com.example.momowas.vote.service;

import com.example.momowas.vote.domain.Vote;
import com.example.momowas.vote.dto.VoteRequest;
import com.example.momowas.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    /* 투표 생성 */
    @Transactional
    public Vote createVote(VoteRequest voteRequest) {
        return voteRepository.save(voteRequest.toEntity());
    }
}
