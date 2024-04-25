package com.projectX.projectX.domain.member.service;

import com.projectX.projectX.domain.member.repository.MemberAuthRepository;
import com.projectX.projectX.global.common.security.dto.RefreshToken;
import com.projectX.projectX.global.common.security.exception.InvalidRefreshTokenException;
import com.projectX.projectX.global.common.security.service.JwtUtil;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final JwtUtil jwtUtil;
    private final MemberAuthRepository memberAuthRepository;

//    @Transactional
//    public void saveTokenInfo(String email, String accessToken, String refreshToken) {
//        memberAuthRepository.save(new RefreshToken(email, accessToken, refreshToken));
//    }

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
            throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

}
