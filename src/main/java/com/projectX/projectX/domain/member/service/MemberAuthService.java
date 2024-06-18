package com.projectX.projectX.domain.member.service;

import com.projectX.projectX.domain.member.exception.InvalidTokenException;
import com.projectX.projectX.domain.member.repository.MemberAuthRepository;
import com.projectX.projectX.global.exception.ErrorCode;
import com.projectX.projectX.global.security.dto.RefreshToken;
import com.projectX.projectX.global.security.util.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final JwtUtil jwtUtil;
    private final MemberAuthRepository memberAuthRepository;

    public String refreshAccessToken(String accessToken) {
        Optional<RefreshToken> tokenResponse = memberAuthRepository.findRefreshByAccessToken(
            accessToken);
        if (tokenResponse.isPresent() && jwtUtil.verifyToken(
            tokenResponse.get().getRefreshToken())) {
            RefreshToken token = tokenResponse.get();
            String newAccessToken = jwtUtil.generateAccessToken(token.getId(),
                jwtUtil.getRole(token.getRefreshToken()));
            token.updateAccessToken(newAccessToken);
            memberAuthRepository.save(token);
            return newAccessToken;
        } else {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }
}