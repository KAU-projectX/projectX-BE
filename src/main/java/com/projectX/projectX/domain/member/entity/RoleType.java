package com.projectX.projectX.domain.member.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ADMIN_USER", "관리자 권한");

    private final String code;
    private final String displayName;
}
