package com.projectX.projectX.domain.member.controller;

import com.projectX.projectX.domain.member.service.MemberService;
import com.projectX.projectX.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/token/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<String> logout(
        @RequestHeader(AUTHORIZATION_HEADER) final String accessToken) {
        memberService.logout(accessToken);
        return ResponseDTO.res("로그아웃에 성공했습니다.");
    }
}
