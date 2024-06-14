package com.projectX.projectX.global.security.handler;

import com.projectX.projectX.global.security.dto.CustomOAuth2User;
import com.projectX.projectX.global.security.dto.GeneratedToken;
import com.projectX.projectX.global.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final String KAKAO_URI = "http://localhost:3000/login";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        //1. oAuth2User로 캐스팅하여 인증된 사용자의 정보를 가져온다.
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        //2. oAuth2User의 정보들을 가져온다.
        String email = customOAuth2User.getEmail();

        String provider = customOAuth2User.getProvider();

        //3. 로그인한 회원 존재의 여부를 가져온다.
        String role = customOAuth2User.getAuthorities().stream()
            .findFirst()
            .orElseThrow(IllegalAccessError::new)
            .getAuthority();

        // jwt token 발행을 시작한다.
        GeneratedToken generatedToken = jwtUtil.generateToken(email, role);

        switch (provider) {
            case "kakao":
                String kakaoRedirectUrl = UriComponentsBuilder.fromUriString(KAKAO_URI)
                    .queryParam("accessToken", generatedToken.accessToken())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
                log.info("카카오 회원 access Token redirect 준비");
                getRedirectStrategy().sendRedirect(request, response, kakaoRedirectUrl);
                break;
        }
    }
}
