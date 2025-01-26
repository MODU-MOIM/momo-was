package com.example.momowas.recommend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 좋아요/댓글 이벤트 처리
    public void handleFeedEvent(Long feedId, String eventType) {
        String key = "PopularFeed";
        int score = calculateScore(eventType);
        String feedIdKey = "feed_" + String.valueOf(feedId);
        redisTemplate.opsForZSet().incrementScore(key, feedIdKey, score);
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
    public List<String> getTopFeeds(int limit) {
        String key = "PopularFeed";
        Set<Object> topFeeds = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);
        return topFeeds.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

}
