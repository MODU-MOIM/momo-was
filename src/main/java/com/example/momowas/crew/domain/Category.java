package com.example.momowas.crew.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    ACTIVITY("액티비티"),
    CULTURE_ART("문화·예술"),
    FOOD("푸드·드링크"),
    HOBBY("취미"),
    TRAVEL("여행"),
    SELF_IMPROVEMENT("자기계발"),
    FINANCE("재테크"),
    GAMING("게임");

    private final String category;
}

