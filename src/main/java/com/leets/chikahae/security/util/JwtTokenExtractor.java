package com.leets.chikahae.security.util;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import com.leets.chikahae.global.response.ErrorCode;
import com.leets.chikahae.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

import static com.leets.chikahae.security.util.TokenNameUtil.*;

/**
 * JWT 토큰 추출 및 검증을 위한 유틸리티 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenExtractor {

    @Value("${chikahae.jwt.key}")
    private String key;

    private SecretKey secretKey;
    private final ParentRepository parentRepository;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    // access 토큰 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }

    // refresh 토큰 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    // 토큰 파싱
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            log.warn("JWT 파싱 실패: {}", e.getMessage());
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN.getMessage());
        }
    }


    // 검증 여부
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            log.info("토큰 유효성 검사 성공: {}", claims);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료됨", e);
            throw new JwtAuthenticationException("토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("토큰 검증 실패: {}", e.getMessage(), e);
            throw new JwtAuthenticationException("토큰이 생성되지 않았습니다."); // 여기가 문제라면 메시지 바꿔도 좋음
        }
    }

    // 인증 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<String> authoritiesList = (List<String>) claims.get("authorities");
        if (authoritiesList == null) {
            log.warn("JWT에 권한 정보(authorities)가 없습니다.");
            authoritiesList = List.of();
        }
        Collection<? extends GrantedAuthority> authorities =
                authoritiesList.stream().map(SimpleGrantedAuthority::new).toList();


        // ID 추출 및 사용자 정보 조회
        Long claimMemberId = claims.get(ID_CLAIM, Long.class);
        Parent parent = parentRepository.findByParentId(claimMemberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));

        // PrincipalDetails 생성
        PrincipalDetails details = PrincipalDetails.of(
                KakaoUserInfo.of(parent.getId(), parent.getEmail(), parent.getName()),
                authorities
        );

        return new UsernamePasswordAuthenticationToken(details, token, authorities);
    }

    // 사용자 정보 추출
    public String getId(String token) {
        return getIdFromToken(token, ID_CLAIM);
    }

    public String getEmail(String token) {
        return getClaimFromToken(token, EMAIL_CLAIM);
    }

    public String getRole(String token) {
        return getClaimFromToken(token, ROLE_CLAIM);
    }

    public Boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private String getClaimFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, String.class);
    }

    private String getIdFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, String.class);
    }
}
