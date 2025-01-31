package com.example.momowas.authorization;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.joinrequest.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CrewManager {

    private final CrewMemberService crewMemberService;
    private final CrewService crewService;

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

    /* 크루 멤버가 일정 생성 권한이 있는지 확인 */
    public boolean hasScheduleCreatePermission(Long crewId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Crew crew = crewService.findCrewById(crewId);
        Role scheduleCreatePermission = crew.getScheduleCreatePermission();

        return validatePermission(scheduleCreatePermission, crewMember);
    }

    /* 크루 멤버가 일정 수정 권한이 있는지 확인 */
    public boolean hasScheduleUpdatePermission(Long crewId, Long userId) {
        CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);
        Crew crew = crewService.findCrewById(crewId);
        Role scheduleUpdatePermission = crew.getScheduleUpdatePermission();

        return validatePermission(scheduleUpdatePermission, crewMember);
    }

    private boolean validatePermission(Role permission, CrewMember crewMember) {
        return switch (permission) {
            case MEMBER -> true;
            case LEADER -> crewMember.getRole().equals(Role.LEADER);
            case ADMIN -> crewMember.getRole().equals(Role.MEMBER) ? false : true;
        };
    }
}
