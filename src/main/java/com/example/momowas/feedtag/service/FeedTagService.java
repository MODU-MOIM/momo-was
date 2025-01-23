package com.example.momowas.feedtag.service;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feedtag.domain.FeedTag;
import com.example.momowas.feedtag.repository.FeedTagRepository;
import com.example.momowas.tag.domain.Tag;
import com.example.momowas.tag.repository.TagRepository;
import com.example.momowas.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedTagService {

    private final FeedTagRepository feedTagRepository;
    private final TagService tagService;
    private final TagRepository tagRepository;

    /* 피드 태그 생성 */
    @Transactional
    public void createFeedTag(List<String> tagNames, Feed feed) {
        tagNames.stream()
                .map(tagService::findOrCreateTag)
                .forEach(tag -> feedTagRepository.save(FeedTag.of(feed, tag)));
    }

    /* 태그 수정 */
    @Transactional
    public void updateTags(List<String> updatedTags, Feed feed) {
        List<Tag> existingTags=feed.getFeedTags().stream()
                .map(FeedTag::getTag).toList();
        List<String> existingTagNames = existingTags.stream()
                .map(Tag::getName).toList();

        // 새로운 태그: 업데이트 태그 중 기존에 없는 태그
        List<String> newTags = updatedTags.stream()
                .filter(tag -> !existingTagNames.contains(tag))
                .collect(Collectors.toList());

        //삭제할 태그: 기존 태그 중 업데이트에 없는 태그
        List<Tag> deleteTags = existingTags.stream()
                .filter(tag -> !updatedTags.contains(tag.getName()))
                .collect(Collectors.toList());

        createFeedTag(newTags, feed);
        deleteTags.forEach(tag->feedTagRepository.deleteByFeedIdAndTagId(feed.getId(), tag.getId())); //피드와 태그 연결 끊음.
        deleteTags(deleteTags);
    }

    /* 태그 사용 여부 확인 후 삭제 */
    @Transactional
    public void deleteTags(List<Tag> tags) {
        for (Tag tag : tags) {
            if(! feedTagRepository.existsByTagId(tag.getId())){
                tagRepository.delete(tag);
            }
        }
    }
}
