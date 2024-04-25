package com.projectX.projectX.global.common.security.handler;

import com.projectX.projectX.global.common.security.dto.GeneratedToken;
import com.projectX.projectX.global.common.security.dto.RefreshToken;
import com.projectX.projectX.global.common.security.service.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final String URI = "http://localhost:8080/sample";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        log.warn("Login Success");
        //1. oAuth2User로 캐스팅하여 인증된 사용자의 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        //2. oAuth2User의 정보들을 가져온다.
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = oAuth2User.getAttribute("email");

        //3. 로그인한 회원 존재의 여부를 가져온다.
        boolean isExist = oAuth2User.getAttribute("exist");
        String role = oAuth2User.getAuthorities().stream()
            .findFirst()
            .orElseThrow(IllegalAccessError::new)
            .getAuthority();

        //3-1. 회원이 존재하는 경우
        if (!isExist) {
            // jwt token 발행을 시작한다.
            GeneratedToken generatedToken = jwtUtil.generateToken(email, role);
            log.info("{}", generatedToken);
            // accessToken을 쿼리스트링에 담는 url을 만든다.
            String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                //.queryParam("accessToken", generatedToken.getAccessToken())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
            log.info("회원 존재 redirect 준비");
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else { //3-2. 회원이 존재하지 않는 경우
            // 프론트와 어떻게 진행할지 아직 미정
            String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("email", (String) oAuth2User.getAttribute("email"))
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
            log.info("회원 미존재 redirect 준비");
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}
