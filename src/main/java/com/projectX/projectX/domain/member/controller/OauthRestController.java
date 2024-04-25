package com.projectX.projectX.domain.member.controller;

import com.projectX.projectX.domain.member.service.MemberAuthService;
import com.projectX.projectX.global.common.ResponseDTO;
import com.projectX.projectX.global.common.security.dto.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oauth")
@RequiredArgsConstructor
public class OauthRestController {

    private final MemberAuthService authService;
    private final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<String> refreshAccessToken(
        @RequestHeader(AUTHORIZATION_HEADER) final String accessToken) {
        return ResponseDTO.res(authService.refreshAccessToken(accessToken), "Access 토큰 재발급에 성공했습니다.");
    }
}
