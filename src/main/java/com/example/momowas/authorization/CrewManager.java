package com.example.momowas.authorization;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.repository.CrewMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CrewManager {

    private final CrewMemberRepository crewMemberRepository;

    /* 크루 멤버인지 확인 */
    @Transactional(readOnly = true)
    public boolean hasCrewPermission(Long crewId, Long userId) {
        return crewMemberRepository.existsByCrewIdAndUserId(crewId, userId);
    }

    /* 크루 멤버가 리더 권한이 있는지 확인 */
    public boolean hasCrewLeaderPermission(Long crewId, Long userId) {
        return crewMemberRepository.findByCrewIdAndUserId(crewId, userId)
                .map(crewMember -> crewMember.getRole().equals(Role.LEADER))
                .orElse(false);
    }
}
