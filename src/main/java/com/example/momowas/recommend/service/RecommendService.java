package com.example.momowas.recommend.service;

import com.example.momowas.archive.domain.Archive;
import com.example.momowas.archive.repository.ArchiveRepository;
import com.example.momowas.archive.service.ArchiveService;
import com.example.momowas.recommend.dto.ArchiveRecommendDto;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final ArchiveRepository archiveRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 좋아요/댓글 이벤트 처리
    public void handleArchiveEvent(Long archiveId, String eventType) {
        String key = "PopularArchive";
        int score = calculateScore(eventType);
        redisTemplate.opsForZSet().incrementScore(key, String.valueOf(archiveId), score);
    }

    // 가중치 설정
    private int calculateScore(String eventType) {
        return switch (eventType) {
            case "createArchive" -> 5;
            case "like" -> 2;
            case "unLike" -> -2;
            case "comment" -> 3;
            case "deleteComment" -> -3;
            default -> 0;
        };
    }

    // 상위 N개의 인기 피드 조회
    public List<ArchiveRecommendDto> getTopArchives(int limit) {
        String key = "PopularArchive";
        List<Long> archiveIds = redisTemplate.opsForZSet()
                .reverseRange(key, 0, limit - 1)
                .stream()
                .map(id -> Long.parseLong(id.toString()))
                .toList();

        return archiveIds.stream()
                .map(id -> {
                    Archive archive = archiveRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ARCHIVE));
                    return  ArchiveRecommendDto.builder()
                            .archiveId(archive.getId())
                            .crewId(archive.getCrew().getId())
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 크루 이벤트 처리
    public void handleCrewEvent(Long crewId, String eventType, int currentMembers, int maxMembers) {
        String key = "PopularCrew";

        Double currentScore = redisTemplate.opsForZSet().score(key, String.valueOf(crewId));
        if (currentScore == null) currentScore = 0.0; // 점수가 없으면 기본값 0

        double memberRatio = (eventType.equals("newMember"))
                ? calculateMemberRatio(currentMembers, maxMembers)
                : 0;
        double eventScore = calculateCrewScore(eventType);
        double updatedScore = currentScore + memberRatio + eventScore;
        redisTemplate.opsForZSet().add(key, String.valueOf(crewId), updatedScore);
    }

    // 멤버 비율 계산
    private double calculateMemberRatio(int currentMembers, int maxMembers) {
        return maxMembers > 0 ? (double) currentMembers / maxMembers * 100 : 0;
    }

    private double calculateCrewScore(String eventType) {
        return switch (eventType) {
            case "createCrew" -> 5;
            case "addSchedule" -> 10;
            case "removeSchedule" -> -10;
            case "addFeed" -> 8;
            case "deleteFeed" -> -8;
            default -> 0;
        };
    }

    // 상위 N개의 인기 크루 조회
    public List<Long> getTopCrewIds(int limit) {
        String key = "PopularCrew";
        return redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1).stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toList());
    }

    public void removeRecommendArchive(Long archiveId) {
        String key = "PopularArchive";
        redisTemplate.opsForZSet().remove(key, String.valueOf(archiveId));
    }

    public void removeRecommendCrew(Long crewId) {
        String key = "PopularCrew";
        redisTemplate.opsForZSet().remove(key, String.valueOf(crewId));
    }



}
