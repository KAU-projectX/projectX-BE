package com.projectX.projectX.global.security.dto;

import lombok.Builder;

public record SecurityUser(
    String email,
    String nickname,
    String role,
    String providerType
) {

    @Builder
    public SecurityUser {
    }
}
