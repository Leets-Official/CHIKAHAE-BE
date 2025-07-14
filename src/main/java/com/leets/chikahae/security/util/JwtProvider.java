package com.leets.chikahae.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

import static com.leets.chikahae.security.util.TokenNameUtil.ID_CLAIM;

/**
 * JWT 토큰을 생성하고 관리하는 클래스
 * */
@Component
public class JwtProvider {

    @Value("${chikahae.jwt.key}")
    private String key;

    @Value("${chikahae.jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${chikahae.jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private SecretKey secretKey;


    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    // Access token 발급
    public String generateAccessToken(Long memberId) {
        return generateToken(memberId,accessTokenExpiration);
    }

    // Refresh token 발급
    public String generateRefreshToken(Long memberId) {
        return generateToken(memberId, refreshTokenExpiration);
    }

    /// 토큰 생성 함수
    public String generateToken(Long memberId, long expireTime) {

        /// 시간 설정
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        /// 인증에서 객체 가져오기
        PrincipalDetails principalDetails= (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 권한 리스트 추출
        Collection<? extends GrantedAuthority> collection = principalDetails.getAuthorities();

        // String 형태로 변환
        List<String> authorities = collection == null ? List.of() : collection.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        /// JWT 내용 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_CLAIM, memberId);
        claims.put("authorities", authorities); // 권한 정보 추가

        // 토큰 반환
        return Jwts.builder()
                .setSubject(String.valueOf(memberId)) // 사용자 Id
                .setClaims(claims)
                .setIssuedAt(now)                                // 발급 시간
                .setExpiration(expiredDate)                      // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS512)   // 서명
                .compact();
    }

}

