package com.example.momowas.crew.dto;

import com.example.momowas.user.domain.Gender;

public record CrewConditionReqDto(Integer minAge,
                                  Integer maxAge,
                                  Gender genderRestriction) {
}
