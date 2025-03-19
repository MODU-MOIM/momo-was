package com.example.momowas.voteparticipant.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.voteparticipant.domain.VoteParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteParticipantRepository extends JpaRepository<VoteParticipant,Long> {
    Optional<VoteParticipant> findByVoteIdAndCrewMemberId(Long voteId, Long crewMemberId);
    boolean existsByVoteIdAndCrewMemberId(Long voteId, Long crewMemberId);
}
