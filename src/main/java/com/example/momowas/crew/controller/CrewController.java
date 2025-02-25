package com.example.momowas.crew.controller;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.dto.*;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;
    private final S3Service s3Service;
    private final JwtUtil jwtUtil;

    /* 크루 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Object> createCrew(@RequestPart CrewReqDto crewReqDto,
                                         @RequestPart(required = false, value = "bannerImage") MultipartFile file ,
                                         @AuthenticationPrincipal Long userId) throws IOException {
        Long crewId = crewService.createCrew(crewReqDto, file, userId);
        return Map.of("crewId", crewId);
    }

    /* 크루 이미지 업로드 */
    @PostMapping("/images")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> uploadCrewImage(@RequestParam("crewImage") MultipartFile file) throws IOException {
        String crewImageUrl = s3Service.uploadImage(file, "crew");
        return Map.of("crewImageUrl", crewImageUrl);
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

    /* 크루명 및 배너 사진 수정 */
    @PatchMapping("/{crewId}/basic")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrewBasic(@RequestPart CrewNameReqDto crewNameReqDto,
                                                  @RequestPart(required = false, value = "bannerImage") MultipartFile file,
                                                  @PathVariable Long crewId,
                                                  @AuthenticationPrincipal Long userId) throws IOException {
        crewService.updateCrewBasic(crewNameReqDto, file, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 크루 소개 수정 */
    @PatchMapping("/{crewId}/report")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrewReport(@RequestBody CrewReportReqDto crewReportReqDto,
                                                   @PathVariable Long crewId,
                                                   @AuthenticationPrincipal Long userId){
        crewService.updateCrewReport(crewReportReqDto, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 크루 인원수 수정 */
    @PatchMapping("/{crewId}/headcount")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrewHeadCount(@RequestBody CrewHeadcountReqDto crewHeadcountReqDto,
                                                      @PathVariable Long crewId,
                                                      @AuthenticationPrincipal Long userId){
        crewService.updateCrewHeadCount(crewHeadcountReqDto, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 크루 가입 조건 수정 */
    @PatchMapping("/{crewId}/condition")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrewCondition(@RequestBody CrewConditionReqDto crewConditionReqDto,
                                                      @PathVariable Long crewId,
                                                      @AuthenticationPrincipal Long userId){
        crewService.updateCrewCondition(crewConditionReqDto, crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 특정 크루 삭제 */
    @DeleteMapping("/{crewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> deleteCrew(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        crewService.deleteCrew(crewId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    @GetMapping("/type")
    public List<CrewDetailResDto> searchCrewByName(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "category", required = false) Category category,
                                                   @RequestParam(value = "age-group", required = false) Integer ageGroup,
                                                   @RequestParam(value = "region", required = false) String depth1) {
        Specification<Crew> spec = (root, query, criteriaBuilder) -> null;

        if (name != null)
            spec = spec.and(CrewSpecification.equalCrewName(name));
        if (category != null)
            spec = spec.and(CrewSpecification.equalCategory(category));
        if (ageGroup != null)
            spec = spec.and(CrewSpecification.equalAgeGroup(ageGroup));
        if (depth1 != null)
            spec = spec.and(CrewSpecification.hasRegion(depth1));

        return crewService.search(spec);
    }

    @GetMapping("/me")
    public List<CrewListResDto> getCrewsByMe(HttpServletRequest request){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return crewService.getCrewsByMe(userId);
    }

}