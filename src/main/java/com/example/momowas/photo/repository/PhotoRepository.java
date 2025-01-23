package com.example.momowas.photo.repository;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.photo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Modifying
    @Query("DELETE FROM Photo p WHERE p.feed = :feed")
    void deleteByFeed(Feed feed);
}
