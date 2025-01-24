package com.example.momowas.comment.dto;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;

public record FeedCommentReqDto(
        String content
) {
    public Comment toEntity(Feed feed, CrewMember crewMember, Comment parent) {
        return Comment.builder()
                .content(content)
                .feed(feed)
                .crewMember(crewMember)
                .parent(parent)
                .build();
    }
}
