package com.example.momowas.recommend.service;

import com.example.momowas.feed.service.FeedService;
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
    public void handleFeedEvent(Long feedId, String eventType) {
        String key = "PopularFeed";
        int score = calculateScore(eventType);
        redisTemplate.opsForZSet().incrementScore(key, String.valueOf(feedId), score);
    }

    // 가중치 설정
    private int calculateScore(String eventType) {
        return switch (eventType) {
            case "like" -> 2;
            case "unLike" -> -2;
            case "comment" -> 3;
            case "deleteComment" -> -3;
            default -> 0;
        };
    }

    // 상위 N개의 인기 피드 조회
    public List<Long> getTopFeedIds(int limit) {
        String key = "PopularFeed";
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

}
