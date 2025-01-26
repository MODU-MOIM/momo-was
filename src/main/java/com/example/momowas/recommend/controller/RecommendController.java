package com.example.momowas.recommend.controller;

import com.example.momowas.feed.dto.FeedListResDto;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    //반환형은.. 일단 나중에 생각/.
    @GetMapping("/feeds/popular")
    public CommonResponse<List<Long>> getTopFeeds(@RequestParam(value="limit") int limit){
        return CommonResponse.of(ExceptionCode.SUCCESS, recommendService.getTopFeedIds(limit));
    }
}
