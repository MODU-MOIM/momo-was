package com.example.momowas.crew.service;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.dto.CreateCrewReqDto;
import com.example.momowas.crew.dto.CrewResDto;
import com.example.momowas.crew.repository.CrewRepository;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.crewregion.service.CrewRegionService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final CrewRepository crewRepository;
    private final CrewMemberService crewMemberService;
    private final CrewRegionService crewRegionService;
    private final UserService userService;

    /* 크루 생성 */
    @Transactional
    public CrewResDto createCrew(CreateCrewReqDto createCrewReqDto, Long userId) {

        //크루명 중복 검증
        if (crewRepository.existsByName(createCrewReqDto.name())) {
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS_CREW);
        }

        Crew crew = crewRepository.save(createCrewReqDto.toEntity()); //크루 저장
        crewRegionService.createCrewRegion(createCrewReqDto.regions(), crew); //크루-지역 저장

        User user = userService.readById(userId);
        crewMemberService.createAdmin(user, crew); //크루 멤버 저장

        return CrewResDto.fromEntity(crew);
    }
}
