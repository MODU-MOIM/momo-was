package com.example.momowas.schedule.dto;

import com.example.momowas.crewmember.domain.Role;

public record SchedulePermissionReqDto(Role createPermission,
                                       Role updatePermission) {
}
