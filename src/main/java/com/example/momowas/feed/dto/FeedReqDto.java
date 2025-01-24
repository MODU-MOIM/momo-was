package com.example.momowas.feed.dto;

import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;

import java.util.List;

public record FeedReqDto(String content,
                         List<String> tagNames) {
    public Feed toEntity(Crew crew, CrewMember crewMember) {
        return Feed.builder()
                .content(content)
                .crew(crew)
                .crewMember(crewMember)
                .build();
    }
}
