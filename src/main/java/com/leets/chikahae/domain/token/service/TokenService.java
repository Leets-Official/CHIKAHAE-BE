package com.leets.chikahae.domain.token.service;

import com.leets.chikahae.security.util.JwtProvider;
import com.leets.chikahae.domain.token.entity.AccountToken;
import com.leets.chikahae.domain.token.repository.AccountTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TokenService {


    private final AccountTokenRepository accountTokenRepository;
    private final JwtProvider jwtProvider;

    public String issueAccessToken(Long memberId) {
        return jwtProvider.generateAccessToken(memberId);
    }

    public String issueRefreshToken(Long memberId) {
        String token= jwtProvider.generateRefreshToken(memberId);
        AccountToken refreshToken = AccountToken.builder()
                .memberId(memberId)
                .tokenType("REFRESH")
                .expiresAt(LocalDateTime.now().plusDays(14))
                .build();

        accountTokenRepository.save(refreshToken);
        return token;
    }









}//class
