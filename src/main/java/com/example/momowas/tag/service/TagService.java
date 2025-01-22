package com.example.momowas.tag.service;

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

    /* 태그 생성(이미 존재하는 태그면 해당 태그 반환) */
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(()->tagRepository.save(Tag.of(tagName)));
    }
}
