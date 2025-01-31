package com.example.momowas.authorization;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.joinrequest.domain.JoinRequest;
import com.example.momowas.joinrequest.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CrewManager {

    private final CrewMemberService crewMemberService;
    private final JoinRequestService joinRequestService;

    /* 크루 멤버인지 확인 */
    @Transactional(readOnly = true)
    public boolean hasCrewPermission(Long crewId, Long userId) {
        return crewMemberService.isCrewMemberExists(userId,crewId);
    }

    /* 크루 멤버가 리더 권한이 있는지 확인 */
    public boolean hasCrewLeaderPermission(Long crewId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        return crewMember.getRole().equals(Role.LEADER);
    }

    @Transactional(readOnly = true)
    public Role findUserRoleInCrew(Long crewId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(crewId, userId);
        return crewMember.getRole();
    }
}
