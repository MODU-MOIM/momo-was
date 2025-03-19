package com.example.momowas.oauth2.helper;

import java.util.Optional;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();

    String getEmail();

    String getProfileImage();

    default Optional<String> getGender() {
        return Optional.empty(); // 기본값: 비어 있음
    }

    default Optional<String> getBirthYear() {
        return Optional.empty(); // 기본값: 비어 있음
    }

    default Optional<String> getMobile() {
        return Optional.empty(); // 기본값: 비어 있음
    }

}
