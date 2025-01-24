package com.example.momowas.feedtag.repository;

import com.example.momowas.feedtag.domain.FeedTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedTagRepository extends JpaRepository<FeedTag,Long> {
    boolean existsByTagId(Long tagId);
    void deleteByFeedIdAndTagId(Long feedId, Long tagId);
}
