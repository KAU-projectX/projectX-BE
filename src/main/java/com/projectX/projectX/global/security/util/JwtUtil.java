package com.projectX.projectX.global.security.util;

import com.projectX.projectX.domain.member.service.MemberService;
import com.projectX.projectX.global.exception.ErrorCode;
import com.projectX.projectX.global.security.dto.CustomOAuth2User;
import com.projectX.projectX.global.security.dto.GeneratedToken;
import com.projectX.projectX.global.security.exception.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final MemberService memberService;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenExpireTime;

    public GeneratedToken generateToken(String email, String role) {
        String accessToken = generateAccessToken(email, role);
        String refreshToken = generateRefreshToken(email, role);
        memberService.saveTokenInfo(email, accessToken, refreshToken);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateAccessToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + accessTokenExpireTime))
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenExpireTime))
            .signWith(key)
            .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return claims.getBody()
                .getExpiration()
                .after(new Date());
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    public Authentication getAuthentication(CustomOAuth2User customOAuth2User) {
        return new UsernamePasswordAuthenticationToken(customOAuth2User, "",
            customOAuth2User.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
            .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
            .get("role", String.class);

    }
}
