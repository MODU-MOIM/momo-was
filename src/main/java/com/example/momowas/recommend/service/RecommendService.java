package com.example.momowas.recommend.service;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.recommend.dto.HotPlaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RedisTemplate<String, Object> redisTemplate;

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
    public List<Long> getTopArchiveIds(int limit) {
        String key = "PopularArchive";
        return redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1).stream()
                .map(id -> Long.parseLong(id.toString()))
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

    //핫플레이스(schedule 설정 시 언급 빈도)
    public void incrementHotPlace(String detailAddress, Category category) {
        String key = "HotPlace:" + category.name();
        redisTemplate.opsForZSet().incrementScore(key, detailAddress, 1);
    }

    public void decrementHotPlace(String detailAddress, Category category) {
        String key = "HotPlace:" + category.name();
        Double score = redisTemplate.opsForZSet().score(key, detailAddress);
        if (score > 1) {
            redisTemplate.opsForZSet().incrementScore(key, detailAddress, -1);
        } else {
            redisTemplate.opsForZSet().remove(key, detailAddress);
        }
    }

    public List<HotPlaceResDto> getTopHotPlaces(Category category, int limit) {
        String key = "HotPlace:" + category.name();
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, limit - 1).stream()
                .map(tuple ->  HotPlaceResDto.builder()
                        .category(category)
                        .detailAddress(tuple.getValue().toString())
                        .build())
                .collect(Collectors.toList());
    }

}
