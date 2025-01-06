package com.example.momowas.oauth2.helper;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
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
        return (String)attributes.get("picture");
    }

    @Override
    public Optional<String> getGender() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getBirthYear() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getMobile() {
        return Optional.empty();
    }
}