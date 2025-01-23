package com.example.momowas.like.repository;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.feed.domain.Feed;
import com.example.momowas.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    boolean existsByFeedAndCrewMember(Feed feed, CrewMember crewMember);
    Optional<Like> findByFeedAndCrewMember(Feed feed, CrewMember crewMember);
}
