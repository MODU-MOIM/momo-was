package com.example.momowas.crewmember.dto;

import com.example.momowas.crew.domain.Role;
import com.example.momowas.crewmember.domain.CrewMember;


public record CrewMemberListResDto(String nickname,
                                   String profileImage,
                                   Role role) {
    public static CrewMemberListResDto of(CrewMember crewMember) {
        return new CrewMemberListResDto(
                crewMember.getUser().getNickname(),
                crewMember.getUser().getProfileImage(),
                crewMember.getRole()
        );
    }
}
