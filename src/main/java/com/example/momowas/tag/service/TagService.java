package com.example.momowas.tag.service;

import com.example.momowas.feedtag.repository.FeedTagRepository;
import com.example.momowas.tag.domain.Tag;
import com.example.momowas.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final FeedTagRepository feedTagRepository;

    /* 태그 생성(이미 존재하는 태그면 해당 태그 반환) */
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(()->tagRepository.save(Tag.of(tagName)));
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
