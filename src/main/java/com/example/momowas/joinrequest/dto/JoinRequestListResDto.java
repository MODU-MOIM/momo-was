package com.example.momowas.joinrequest.dto;

import com.example.momowas.joinrequest.domain.JoinRequest;
import com.example.momowas.joinrequest.domain.RequestStatus;
import com.example.momowas.user.domain.Gender;
import com.example.momowas.user.domain.User;

public record JoinRequestListResDto(Long joinRequestId,
                                    String nickname,
                                    Gender gender,
                                    Integer age,
                                    String profileImage,
                                    RequestStatus requestStatus) {
    public static JoinRequestListResDto of(JoinRequest joinRequest, User user) {
        return new JoinRequestListResDto(
                joinRequest.getId(),
                user.getNickname(),
                user.getGender(),
                user.getAge(),
                user.getProfileImage(),
                joinRequest.getRequestStatus()
        );
    }
}
