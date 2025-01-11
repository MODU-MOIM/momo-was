package com.example.momowas.crewmember.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.repository.CrewMemberRepository;
import com.example.momowas.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;

    /* 크루 멤버(리더) 생성 */
    @Transactional
    public CrewMember createLeader(User user, Crew crew) {
        CrewMember crewMember = CrewMember.of(crew, user, Role.LEADER);
        return crewMemberRepository.save(crewMember);
    }
}
