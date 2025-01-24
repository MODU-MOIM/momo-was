package com.example.momowas.record.controller;

import com.example.momowas.record.dto.ArchiveReqDto;
import com.example.momowas.record.service.ArchiveService;
import com.example.momowas.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/crews/{crewId}/archives")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;
    private final S3Service s3Service;

    /* 기록 생성 */
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @crewManager.hasCrewLeaderPermission(#crewId, #userId)") //Leader 권한만 호출 가능하도록
    public Map<String, Object> createArchive(@RequestBody ArchiveReqDto archiveReqDto,
                                            @PathVariable Long crewId,
                                            @AuthenticationPrincipal Long userId) {
        Long archiveId = archiveService.createArchive(archiveReqDto, crewId, userId);
        return Map.of("archiveId",archiveId);
    }

    /* 기록 이미지 업로드 */
    @PostMapping("/images")
    public Map<String, Object> uploadArchiveImage(@RequestParam("archiveImage") MultipartFile file) throws IOException {
        String archiveImageUrl = s3Service.uploadImage(file, "archive");
        return Map.of("archiveImageUrl", archiveImageUrl);
    }
}
