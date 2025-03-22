package com.example.momowas.recommend.controller;

import com.example.momowas.recommend.dto.ArchiveRecommendDto;
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
    @GetMapping("/archives/popular")
    public List<ArchiveRecommendDto> getTopFeeds(@RequestParam(value="limit") int limit){
        return recommendService.getTopArchives(limit);
    }

    @GetMapping("/crews/popular")
    public CommonResponse<List<Long>> getTopCrews(@RequestParam(value="limit") int limit){
        return CommonResponse.of(ExceptionCode.SUCCESS, recommendService.getTopCrewIds(limit));
    }

}
