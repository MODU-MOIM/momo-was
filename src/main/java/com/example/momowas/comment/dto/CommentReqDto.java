package com.example.momowas.comment.dto;

import com.example.momowas.comment.domain.Comment;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.record.domain.Archive;

public record CommentReqDto(
        String content
) {

    public Comment toEntity(Archive archive, CrewMember crewMember, Comment parent) {
        return Comment.archiveCommentBuilder()
                .content(content)
                .archive(archive)
                .crewMember(crewMember)
                .parent(parent)
                .build();
    }
    public Comment toEntity(Feed feed, CrewMember crewMember, Comment parent) {
        return Comment.feedCommentBuilder()
                .content(content)
                .feed(feed)
                .crewMember(crewMember)
                .parent(parent)
                .build();
    }
}
