package com.example.momowas.crew.controller;

import com.example.momowas.crew.dto.CreateCrewReqDto;
import com.example.momowas.crew.dto.CrewResDto;
import com.example.momowas.crew.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/crews")
public class CrewController {
    private final CrewService crewService;

    /* 크루 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public CrewResDto createCrew(@RequestBody CreateCrewReqDto createCrewReqDto, @AuthenticationPrincipal Long userId){
        return crewService.createCrew(createCrewReqDto, userId);
    }


}