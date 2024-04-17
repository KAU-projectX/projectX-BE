package com.projectX.projectX.domain.member.dto.response;

import lombok.Builder;


public record MemberSignUpResponse(
    String userId,
    String userPassword,
    String userNickName
) {

    @Builder
    public MemberSignUpResponse {
    }
}
