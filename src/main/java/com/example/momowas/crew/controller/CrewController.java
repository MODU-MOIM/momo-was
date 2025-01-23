package com.example.momowas.crew.controller;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crew.dto.CrewDetailResDto;
import com.example.momowas.crew.dto.CrewListResDto;
import com.example.momowas.crew.dto.CrewReqDto;
import com.example.momowas.crew.dto.CrewSpecification;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
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

    /* 크루 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Object> createCrew(@RequestPart CrewReqDto crewReqDto,
                                         @RequestPart(required = false, value = "bannerImage") MultipartFile image ,
                                         @AuthenticationPrincipal Long userId) throws IOException {
        String bannerImageUrl = s3Service.uploadImage(image, "crew");
        Long crewId = crewService.createCrew(crewReqDto, bannerImageUrl, userId);
        return Map.of("crewId", crewId);
    }

    /* 크루 이미지 업로드 */
    @PostMapping("/images")
    public Map<String, Object> uploadCrewImage(@RequestParam("crewImage") MultipartFile image) throws IOException {
        String crewImageUrl = s3Service.uploadImage(image, "crew");
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

    /* 특정 크루 수정 */
    @PutMapping("/{crewId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public CommonResponse<String> updateCrew (@RequestPart CrewReqDto crewReqDto,
                                              @RequestPart(required = false, value = "bannerImage") MultipartFile image,
                                              @PathVariable Long crewId,
                                              @AuthenticationPrincipal Long userId) throws IOException {
        String bannerImageUrl= image!=null ? s3Service.uploadImage(image, "crew") : null;
        crewService.updateCrew(crewReqDto, crewId, bannerImageUrl);
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
                                             @RequestParam(value = "category", required = false) Category category) {
        Specification<Crew> spec = (root, query, criteriaBuilder) -> null;

        if (name != null)
            spec = spec.and(CrewSpecification.equalCrewName(name));
        if (category != null)
            spec = spec.and(CrewSpecification.equalCategory(category));

        return crewService.search(spec);
    }

}