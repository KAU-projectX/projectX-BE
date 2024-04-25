package com.projectX.projectX.domain.member.service;

import com.projectX.projectX.domain.member.repository.MemberAuthRepository;
import com.projectX.projectX.global.common.security.dto.RefreshToken;
import com.projectX.projectX.global.common.security.exception.InvalidRefreshTokenException;
import com.projectX.projectX.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberAuthRepository memberAuthRepository;

    @Transactional
    public void logout(String accessToken) {
        RefreshToken refreshToken = memberAuthRepository.findRefreshByAccessToken(
            accessToken).orElseThrow(() -> new InvalidRefreshTokenException(
            ErrorCode.INVALID_REFRESH_TOKEN));
        memberAuthRepository.delete(refreshToken);
    }

    @Transactional
    public void saveTokenInfo(String email, String accessToken, String refreshToken) {
        memberAuthRepository.save(new RefreshToken(email, accessToken, refreshToken));
    }

}
