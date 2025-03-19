package com.example.momowas.recommend.controller;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.recommend.dto.HotPlaceResDto;
import com.example.momowas.recommend.service.RecommendService;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/crews/popular")
    public CommonResponse<List<Long>> getTopCrews(@RequestParam(value="limit") int limit){
        return CommonResponse.of(ExceptionCode.SUCCESS, recommendService.getTopCrewIds(limit));
    }

    @GetMapping("/regions/hot")
    public List<HotPlaceResDto> getTopCrews(@RequestParam(value="limit") int limit, @RequestParam(value = "category") Category category){
        return recommendService.getTopHotPlaces(category, limit);
    }
}
