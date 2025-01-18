package com.example.momowas.voteparticipant.repository;

import com.example.momowas.voteparticipant.domain.VoteParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteParticipantRepository extends JpaRepository<VoteParticipant,Long> {
}
