package com.leets.chikahae.domain.token.service;


import com.leets.chikahae.domain.auth.util.JwtProvider;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.token.entity.AccountToken;
import com.leets.chikahae.domain.token.repository.AccountTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
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
        // 1. refresh 토큰 문자열 생성
        String refreshTokenString = jwtProvider.generateRefreshToken(member.getId());

        // 2. refresh 토큰 DB에 저장
        AccountToken refreshToken = AccountToken.builder()
                .member(member)
                .tokenType("REFRESH")
                .refreshToken(refreshTokenString)  // ✅ 여기서 전달해야 함
                .expiresAt(LocalDateTime.now().plusDays(14))
                .build();

        accountTokenRepository.save(refreshToken);
        return refreshTokenString;
    }

    public void logoutByRefreshToken(String refreshToken) {
        log.info("🔐 로그아웃 요청된 refreshToken: {}", refreshToken);
        accountTokenRepository.deleteByRefreshToken(refreshToken);
    }



    //회원탈퇴
    public void deleteByMemberId(Long memberId) {
        accountTokenRepository.deleteByMemberId(memberId);
    }

    //로그아웃
    @Value("${chikahae.jwt.key}")
    private String jwtKey;
    public Long extractMemberIdFromAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKey)  // 🔑 JWT 서명 키 (Base64 인코딩된 문자열)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.valueOf(claims.getSubject()); // subject가 memberId라고 가정
        } catch (Exception e) {
            // 예외 발생 시 null 반환
            log.warn("⚠️ 유효하지 않은 토큰: {}", e.getMessage());
            return null;
        }
    }














}//class
