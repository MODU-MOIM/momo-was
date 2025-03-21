package com.example.momowas.joinrequest.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.joinrequest.domain.JoinRequest;
import com.example.momowas.joinrequest.domain.RequestStatus;
import com.example.momowas.joinrequest.dto.JoinRequestListResDto;
import com.example.momowas.joinrequest.repository.JoinRequestRepository;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final UserService userService;
    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final RecommendService recommendService;

    @Transactional(readOnly = true)
    public JoinRequest findJoinRequestById(Long joinRequestId) {
        return joinRequestRepository.findById(joinRequestId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_JOIN_REQUEST));
    }

    /* 크루 가입 요청 */
    @Transactional
    public Long createJoinRequest(Long crewId, Long userId) {

        User user = userService.findUserById(userId);
        Crew crew = crewService.findCrewById(crewId);

        validateCrewJoinEligibility(user, crew);

        JoinRequest joinRequest = joinRequestRepository.save(JoinRequest.of(crew, user, RequestStatus.PENDING));
        return joinRequest.getId();
    }

    /* 크루 가입 요청 목록 조회 */
    @Transactional(readOnly = true)
    public List<JoinRequestListResDto> getJoinRequestList(Long crewId) {
        Crew crew = crewService.findCrewById(crewId);

        return crew.getJoinRequests().stream()
                .map(joinRequest -> JoinRequestListResDto.of(joinRequest,joinRequest.getUser()))
                .collect(Collectors.toList());
    }

    /* 크루 가입 요청 수락 */
    @Transactional
    public Long acceptJoinRequest(Long joinRequestId) {
        JoinRequest joinRequest = findJoinRequestById(joinRequestId);

        joinRequest.updateRequestStatus(RequestStatus.ACCEPTED);
        CrewMember crewMember = crewMemberService.createMember(joinRequest.getUser(), joinRequest.getCrew());

        Crew crew = crewMember.getCrew();
        recommendService.handleCrewEvent(crew.getId(), "newMember", crew.getCrewMembers().size(), crew.getMaxMembers());
        return crewMember.getId();
    }

    /* 크루 가입 요청 거절 */
    @Transactional
    public void rejectJoinRequest(Long joinRequestId) {
        JoinRequest joinRequest = findJoinRequestById(joinRequestId);
        joinRequest.updateRequestStatus(RequestStatus.REJECTED);
    }

    /* 사용자가 크루에 가입 가능한지 검증 */
    @Transactional(readOnly = true)
    private void validateCrewJoinEligibility(User user, Crew crew) {
        //이미 사용자가 크루 멤버인지 검증
        if (crewMemberService.isCrewMemberExists(user.getId(), crew.getId())) {
            CrewMember crewMember = crewMemberService.findCrewMemberByCrewAndUser(user.getId(), crew.getId());
            if (crewMember.getDeletedAt() != null) { //탈퇴 전적 검증
                throw new BusinessException(ExceptionCode.BANNED_FROM_CREW);
            }
            throw new BusinessException(ExceptionCode.ALREADY_JOINED_CREW);
        }

        //이미 사용자가 크루에 가입 요청을 했고 PENDING(수락 대기) 상태인지 검증
        joinRequestRepository.findByCrewIdAndUserId(crew.getId(), user.getId())
                .ifPresent(joinRequest -> {
                    if(joinRequest.getRequestStatus()==RequestStatus.PENDING){
                        throw new BusinessException(ExceptionCode.ALREADY_REQUESTED_TO_JOIN_CREW);
                    }
                });

        //크루 정원 초과인지 검증
        if(crew.isCrewFull()){
            throw new BusinessException(ExceptionCode.CREW_FULL);
        }
        //크루 성별 조건 검증
        if(crew.getGenderRestriction()!=null & crew.getGenderRestriction()!=user.getGender()){
            throw new BusinessException(ExceptionCode.INVALID_CREW_JOIN_CONDITION);
        }
        //크루 나이 조건 검증
        if(crew.getMinAge()!=null && user.getAge()< crew.getMinAge() ){
            throw new BusinessException(ExceptionCode.INVALID_CREW_JOIN_CONDITION);
        }
        if(crew.getMaxAge()!=null && user.getAge() > crew.getMaxAge()){
            throw new BusinessException(ExceptionCode.INVALID_CREW_JOIN_CONDITION);
        }

    }
}
