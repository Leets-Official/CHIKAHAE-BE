package com.leets.chikahae.domain.token.service;


import com.leets.chikahae.domain.auth.util.JwtProvider;
import com.leets.chikahae.domain.member.entity.Member;
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

    /**
     * 기존 호출용: IP/UA 없이 액세스 토큰 발급
     */
    public String issueAccessToken(Member member) {
        // null, null 을 넘겨 3-arg 메서드 재사용
        return issueAccessToken(member, null, null);
    }

    //IP/User-Agent 정보를 기록하며 액세스 토큰 발급
    // PrincipalDetails 없이 발급 (기존용)
    public String issueAccessToken(Member member, String ipAddress, String userAgent) {
        AccountToken accessToken = AccountToken.builder()
                .member(member)
                .tokenType("ACCESS")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        accountTokenRepository.save(accessToken);

        // PrincipalDetails 없이 최소한의 토큰
        return jwtProvider.generateAccessToken(member.getId());
    }


    //refresh 토큰 발급 및 저장
    public String issueRefreshToken(Member member) {
     //   String token= jwtProvider.generateRefreshToken(member);

        AccountToken refreshToken = AccountToken.builder()
                .member(member)
                .tokenType("REFRESH")
                .expiresAt(LocalDateTime.now().plusDays(14))
                .build();

        accountTokenRepository.save(refreshToken);
        return jwtProvider.generateRefreshToken(member.getId());
    }









}//class
