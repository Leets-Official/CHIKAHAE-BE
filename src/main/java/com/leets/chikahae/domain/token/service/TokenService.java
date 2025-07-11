package com.leets.chikahae.domain.token.service;

import com.leets.chikahae.domain.token.entity.AccountToken;
import com.leets.chikahae.domain.token.repository.AccountTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {


    private final AccountTokenRepository accountTokenRepository;

    public String issueAccessToken(Long memberId, String ipAddress, String userAgent) {
        String token = UUID.randomUUID().toString(); // 실제는 JWT 발급

        AccountToken accessToken = AccountToken.builder()
                .memberId(memberId)
                .tokenType("ACCESS")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();

        accountTokenRepository.save(accessToken);
        return token;
    }

    public String issueRefreshToken(Long memberId) {
        String token = UUID.randomUUID().toString();

        AccountToken refreshToken = AccountToken.builder()
                .memberId(memberId)
                .tokenType("REFRESH")
                .expiresAt(LocalDateTime.now().plusDays(14))
                .build();

        accountTokenRepository.save(refreshToken);
        return token;
    }









}//class
