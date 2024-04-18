package com.projectX.projectX.domain.member.controller;

import com.projectX.projectX.domain.member.dto.request.MemberSignUpRequest;
import com.projectX.projectX.domain.member.dto.response.MemberSignUpResponse;
import com.projectX.projectX.domain.member.service.MemberService;
import com.projectX.projectX.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<MemberSignUpResponse> memberSignUp(
        @RequestBody MemberSignUpRequest memberSignUpRequest) {
        return ResponseDTO.res(memberService.memberSignUp(memberSignUpRequest), "회원가입에 성공했습니다.");
    }
}
