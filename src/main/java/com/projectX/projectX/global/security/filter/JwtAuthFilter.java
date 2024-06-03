package com.projectX.projectX.global.security.filter;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.global.exception.ErrorCode;
import com.projectX.projectX.global.security.dto.SecurityUser;
import com.projectX.projectX.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("token/") || request.getRequestURI()
            .contains("v1/cafe/") || request.getRequestURI().contains("v1/tour/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        //1. request Header에서 Access Token을 가져온다.
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);

        //2. accessToken이 없을 경우 검사 생략
        if (!StringUtils.hasText(accessToken)) {
            doFilter(request, response, filterChain);
            return;
        }
        //3. AccessToken 검증
        if (accessToken != null && jwtUtil.verifyToken(accessToken)) {
            Member findMember = memberRepository.findByUserEmail(jwtUtil.getEmail(accessToken))
                .orElseThrow(() -> new InvalidMemberException(ErrorCode.INVALID_MEMBER_EXCEPTION));

            //4. SecurityContext에 등록할 User 객체 생성
            SecurityUser securityUser = SecurityUser.builder()
                .email(findMember.getUserEmail())
                .role(findMember.getUserRole().toString())
                .nickname(findMember.getUserNickName())
                .build();

            //5. SecurityContext에 인증 객체 등록
            Authentication authentication = jwtUtil.getAuthentication(securityUser);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new JwtException(ErrorCode.EXPIRED_TOKEN.getSimpleMessage());
        }

        filterChain.doFilter(request, response);
    }
}