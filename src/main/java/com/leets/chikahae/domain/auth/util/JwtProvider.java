package com.leets.chikahae.domain.auth.util;

import com.leets.chikahae.security.auth.PrincipalDetails;
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


    // Access token 발급 - PrincipalDetails 기반
    public String generateAccessToken(PrincipalDetails principalDetails, Long memberId) {
        return generateToken(principalDetails, memberId, accessTokenExpiration);
    }

    // Refresh token 발급 - PrincipalDetails 기반
    public String generateRefreshToken(PrincipalDetails principalDetails, Long memberId) {
        return generateToken(principalDetails, memberId, refreshTokenExpiration);
    }

    // 권한 없이 발급하는 기본 버전
    public String generateAccessToken(Long memberId) {
        return generateToken(memberId, accessTokenExpiration);
    }

    public String generateRefreshToken(Long memberId) {
        return generateToken(memberId, refreshTokenExpiration);
    }



    //토큰 생성 함수
    public String generateToken(PrincipalDetails principalDetails, Long memberId, long expireTime) {
        // 권한 리스트 추출
        Collection<? extends GrantedAuthority> collection = principalDetails.getAuthorities();
        List<String> authorities = collection == null ? List.of() :
                collection.stream().map(GrantedAuthority::getAuthority).toList();

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_CLAIM, memberId);
        claims.put("authorities", authorities);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 권한 없이 memberId만으로 토큰 생성하는 오버로드
    public String generateToken(Long memberId, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_CLAIM, memberId);
        claims.put("authorities", List.of()); // 빈 권한

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }


}//class
