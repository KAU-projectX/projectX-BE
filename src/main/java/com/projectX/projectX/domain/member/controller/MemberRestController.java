package com.projectX.projectX.domain.member.controller;

import com.projectX.projectX.domain.member.service.MemberService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member api", description = "member 관련 API")
@RestController
@RequestMapping("v1/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/token/logout")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "로그아웃 API", description = "회원 로그아웃을 할 수 있는 API입니다.")
    public ResponseDTO<String> logout(
        @RequestHeader(AUTHORIZATION_HEADER) final String accessToken) {
        memberService.logout(accessToken);
        return ResponseDTO.res("로그아웃에 성공했습니다.");
    }
}
