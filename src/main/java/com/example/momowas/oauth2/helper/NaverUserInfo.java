package com.example.momowas.oauth2.helper;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getProfileImage() {
        return (String)attributes.get("profile_image");
    }

    @Override
    public Optional<String> getGender() {
        return Optional.ofNullable((String) attributes.get("gender"));
    }

    @Override
    public Optional<String> getBirthYear() {
        return Optional.ofNullable((String) attributes.get("birthyear"));
    }

    @Override
    public Optional<String> getMobile() {
        return Optional.ofNullable((String) attributes.get("mobile"));
    }

}
