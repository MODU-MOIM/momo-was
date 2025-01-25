package com.example.momowas.record.controller;

import com.example.momowas.record.dto.ArchiveListResDto;
import com.example.momowas.record.dto.ArchiveReqDto;
import com.example.momowas.record.service.ArchiveService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/archives")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;
    private final S3Service s3Service;

    /* 기록 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> createArchive(@RequestBody ArchiveReqDto archiveReqDto,
                                             @PathVariable Long crewId,
                                             @AuthenticationPrincipal Long userId) {
        Long archiveId = archiveService.createArchive(archiveReqDto, crewId, userId);
        return Map.of("archiveId", archiveId);
    }

    /* 기록 이미지 업로드 */
    @PostMapping("/images")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public Map<String, Object> uploadArchiveImage(@RequestParam("archiveImage") MultipartFile file) throws IOException {
        String archiveImageUrl = s3Service.uploadImage(file, "archive");
        return Map.of("archiveImageUrl", archiveImageUrl);
    }

    /* 전체 기록 조회 */
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public List<ArchiveListResDto> getArchiveList(@PathVariable Long crewId, @AuthenticationPrincipal Long userId) {
        return archiveService.getArchiveList();
    }

    /* 기록 수정 */
    @PutMapping("/{archiveId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> updateArchive(@RequestBody ArchiveReqDto archiveReqDto,
                                                @PathVariable Long crewId,
                                                @PathVariable Long archiveId,
                                                @AuthenticationPrincipal Long userId) {
        archiveService.updateArchive(archiveReqDto, crewId, archiveId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }

    /* 기록 삭제 */
    @DeleteMapping("/{archiveId}")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewPermission(#crewId, #userId)") //크루 멤버인지 확인
    public CommonResponse<String> deleteArchive(@PathVariable Long crewId,
                                                @PathVariable Long archiveId,
                                                @AuthenticationPrincipal Long userId) {
        archiveService.deleteArchive(crewId, archiveId, userId);
        return CommonResponse.of(ExceptionCode.SUCCESS,null);
    }
}
