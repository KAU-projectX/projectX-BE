package com.projectX.projectX.global.config;

import com.projectX.projectX.global.security.filter.JwtAuthFilter;
import com.projectX.projectX.global.security.filter.JwtExceptionFilter;
import com.projectX.projectX.global.security.handler.OAuthAuthenticationFailureHandler;
import com.projectX.projectX.global.security.handler.OAuthAuthenticationSuccessHandler;
import com.projectX.projectX.global.security.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthService oauthService;
    private final OAuthAuthenticationSuccessHandler successHandler;
    private final OAuthAuthenticationFailureHandler failureHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable()) //csrf 비활성화
            .cors((cors) -> cors.disable()) //cors 비활성화
            .formLogin((formLogin) -> formLogin.disable()) //form 로그인 비활성화
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션사용 x
            )
            .authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests.anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login((oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
                    .userService(oauthService))
                .successHandler(successHandler)));

        return http
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
            .build();
    }
}
