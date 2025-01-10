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

    public Region findRegion(String depth1, String depth2, String depth3) {
        return regionRepository.findByRegionDepth1AndRegionDepth2AndRegionDepth3(depth1, depth2, depth3)
                .orElseThrow(()->new BusinessException(ExceptionCode.NOT_FOUND_REGION));
    }

}
