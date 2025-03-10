package com.example.momowas.crewmember.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.dto.CrewMemberListResDto;
import com.example.momowas.crewmember.dto.CrewMemberRoleReqDto;
import com.example.momowas.crewmember.repository.CrewMemberRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /* 크루 멤버(일반) 생성  */
    @Transactional
    public CrewMember createMember(User user, Crew crew) {
        //이미 사용자가 크루에 가입했는지 검증
        if (isCrewMemberExists(user.getId(), crew.getId())) {
            new BusinessException(ExceptionCode.ALREADY_JOINED_CREW);
        }
        CrewMember crewMember = CrewMember.of(crew, user, Role.MEMBER);
        return crewMemberRepository.save(crewMember);
    }

    /* 크루 멤버 조회 */
    @Transactional(readOnly = true)
    public CrewMember findCrewMemberByCrewAndUser(Long userId, Long crewId) {
        return crewMemberRepository.findByCrewIdAndUserId(crewId, userId).orElseThrow(() ->
            new BusinessException(ExceptionCode.NOT_FOUND_CREW_MEMBER));
    }

    /* 특정 크루 멤버가 존재하는지 조회 */
    @Transactional(readOnly = true)
    public boolean isCrewMemberExists(Long userId, Long crewId) {
        return crewMemberRepository.existsByCrewIdAndUserId(crewId,userId);
    }

    /* 크루 멤버 Id로 크루 멤버 조회 */
    @Transactional(readOnly = true)
    public CrewMember findCrewMemberById(Long crewMemberId) {
        return crewMemberRepository.findById(crewMemberId).orElseThrow(() ->
                new BusinessException(ExceptionCode.NOT_FOUND_CREW_MEMBER));
    }

    /* 전체 크루 멤버 조회 */
    @Transactional(readOnly = true)
    public List<CrewMemberListResDto> getCrewMemberList(Long crewId) {
        return crewMemberRepository.findByCrewIdAndDeletedAtIsNull(crewId).stream().map(
                CrewMemberListResDto::of).toList();
    }
    @Transactional(readOnly = true)
    public CrewMember getCrewLeader(Long crewId) {
        return crewMemberRepository.findByCrewIdAndRole(crewId, Role.LEADER).orElseThrow(() ->
                new BusinessException(ExceptionCode.NOT_FOUND_CREW_MEMBER));
    }

    /* 특정 크루 멤버 강제 탈퇴 */
    @Transactional
    public void deleteCrewMember(Long crewMemberId) {
        CrewMember crewMember = findCrewMemberById(crewMemberId);
        crewMember.updateDeletedAt(); //soft delete
    }

    /* 크루 멤버 권한 설정 */
    @Transactional
    public void updateCrewMemberRole(CrewMemberRoleReqDto crewMemberRoleReqDto, Long crewMemberId) {
        CrewMember crewMember = findCrewMemberById(crewMemberId);
        crewMember.updateRole(crewMemberRoleReqDto.role());
    }

    /* 크루 리더 위임 */
    @Transactional
    public void delegateCrewLeader(Long crewMemberId, Long crewId, Long userId) {
        CrewMember postLeader = findCrewMemberById(crewMemberId);
        CrewMember preLeader = findCrewMemberByCrewAndUser(userId, crewId);
        preLeader.updateRole(Role.MEMBER);
        postLeader.updateRole(Role.LEADER);
    }
}
