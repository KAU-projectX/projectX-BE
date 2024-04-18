package com.projectX.projectX.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

public record MemberSignUpRequest(
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    String userId,

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 14, message = "비밀번호는 8자 이상, 14자 이하로 입력해주세요.")
    String userPassword,

    @NotEmpty(message = "닉네임는 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,14}$", message = "닉네임은 특수문자를 제외한 2~14자리여야 합니다.")
    String userNickName

) {

    @Builder
    public MemberSignUpRequest {
    }
}
