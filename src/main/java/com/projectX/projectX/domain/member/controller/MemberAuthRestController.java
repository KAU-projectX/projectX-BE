package com.projectX.projectX.domain.member.controller;

import com.projectX.projectX.domain.member.service.MemberAuthService;
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

@Tag(name = "member Auth api", description = "member Auth 관련 API")
@RestController
@RequestMapping("/v1/oauth")
@RequiredArgsConstructor
public class MemberAuthRestController {

    private final MemberAuthService authService;
    private final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "토큰 재발급 API", description = "토큰 만료시, 재발급 받을 수 있는 API입니다.")
    public ResponseDTO<String> refreshAccessToken(
        @RequestHeader(AUTHORIZATION_HEADER) final String accessToken) {
        return ResponseDTO.res(authService.refreshAccessToken(accessToken), "Access 토큰 재발급에 성공했습니다.");
    }

}
