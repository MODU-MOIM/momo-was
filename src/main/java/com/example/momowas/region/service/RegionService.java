package com.example.momowas.region.service;

import com.example.momowas.region.domain.Region;
import com.example.momowas.region.repository.RegionRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    /* 시, 군, 구로 지역 찾기 */
    public Region findRegion(String depth1, String depth2) {
        return regionRepository.findByRegionDepth1AndRegionDepth2(depth1, depth2)
                .orElseThrow(()->new BusinessException(ExceptionCode.NOT_FOUND_REGION));
    }

    /* 지역 id로 지역 찾기 */
    public Region findRegionByRegionId(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(()->new BusinessException(ExceptionCode.NOT_FOUND_REGION));
    }

}
