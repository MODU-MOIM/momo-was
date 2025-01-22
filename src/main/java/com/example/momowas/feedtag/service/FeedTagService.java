package com.example.momowas.feedtag.service;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.feedtag.domain.FeedTag;
import com.example.momowas.feedtag.repository.FeedTagRepository;
import com.example.momowas.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedTagService {

    private final FeedTagRepository feedTagRepository;
    private final FeedService feedService;
    private final TagService tagService;

    /* 피드 태그 생성 */
    @Transactional
    public void createFeedTag(List<String> tagNames, Long feedId) {
        Feed feed = feedService.findFeedById(feedId);

        tagNames.stream()
                .map(tagService::findOrCreateTag)
                .forEach(tag-> feedTagRepository.save(FeedTag.of(feed, tag)));
    }
}
