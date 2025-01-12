package com.example.momowas.crew.controller;

import com.example.momowas.crew.dto.CrewDetailResDto;
import com.example.momowas.crew.dto.CrewListResDto;
import com.example.momowas.crew.dto.CrewReqDto;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;

    /* 크루 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Object> createCrew(@RequestBody CrewReqDto crewReqDto, @AuthenticationPrincipal Long userId){
        return Map.of("crewId",crewService.createCrew(crewReqDto, userId));
    }

    /* 전체 크루 조회 */
    @GetMapping("")
    public List<CrewListResDto> getCrewList() {
        return crewService.getCrewList();
    }

    /* 특정 크루 조회 */
    @GetMapping("/{crewId}")
    public CrewDetailResDto getCrewDetail(@PathVariable Long crewId) {
        return crewService.getCrewDetail(crewId);
    }

    /* 특정 크루 수정 */
    @PutMapping("/{crewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrew (@RequestBody CrewReqDto crewReqDto, @PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        crewService.updateCrew(crewReqDto, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 특정 크루 삭제 */
    @DeleteMapping("/{crewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> deleteCrew(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        crewService.deleteCrew(crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }






}