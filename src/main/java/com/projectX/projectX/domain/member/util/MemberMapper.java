package com.projectX.projectX.domain.member.util;

import com.projectX.projectX.domain.member.dto.request.MemberSignUpRequest;
import com.projectX.projectX.domain.member.dto.response.MemberSignUpResponse;
import com.projectX.projectX.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMapper {

    public static Member toMember(MemberSignUpRequest memberSignUpRequest) {
        return Member.builder()
            .userPassword(memberSignUpRequest.userPassword())
            .userNickName(memberSignUpRequest.userNickName())
            .build();
    }

    public static MemberSignUpResponse toMemberSignUpResponse(Member member) {
        return MemberSignUpResponse.builder()
            .userPassword(member.getUserPassword())
            .userNickName(member.getUserNickName())
            .build();
    }

}
