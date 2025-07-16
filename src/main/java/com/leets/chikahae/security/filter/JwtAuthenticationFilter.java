package com.leets.chikahae.security.filter;


import com.leets.chikahae.global.response.ErrorCode;
import com.leets.chikahae.security.exception.JwtAuthenticationException;
import com.leets.chikahae.security.jwt.JwtTokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.leets.chikahae.global.response.ErrorCode.*;
import static com.leets.chikahae.global.config.SecurityConfig.PERMIT_ALL_PATTERNS;

/**
 * - JWT 토큰을 추출하고 검증하는 필터
 * - 인증이 성공하면 SecurityContextHolder에 인증 정보를 저장
 * - 실패 시 JwtAuthenticationFailureHandler를 통해 에러 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenExtractor extractor;
    private final JwtAuthenticationFailureHandler failureHandler;
    public final static String JWT_ERROR = "jwtError";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출 및 검증
        try {
            Optional<String> token = extractor.extractAccessToken(request);

            // 비어있는지 확인
            if (token.isEmpty()) {
                request.setAttribute(JWT_ERROR, TOKEN_NOT_FOUND);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_NOT_FOUND.getMessage());
            }

            String accessToken = token.get();

            // 타당한지 확인
            if (!extractor.validateToken(accessToken)) {
                request.setAttribute(JWT_ERROR, INVALID_TOKEN);
                throw new JwtAuthenticationException(INVALID_TOKEN.getMessage());
            }

            // 만료가 안되었는지 확인
            if (extractor.isExpired(accessToken)) {
                request.setAttribute(JWT_ERROR, TOKEN_EXPIRED);
                throw new JwtAuthenticationException(TOKEN_EXPIRED.getMessage());
            }

            // 권한 생성
            var authentication = extractor.getAuthentication(token.get());

            // 시큐리티 홀더에 해당 멤버 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {

            /// 실패 핸들러로 이동
            failureHandler.commence(request, response, ex);
        }
    }

    private static final PathPatternParser parser = new PathPatternParser();
    private static final List<PathPattern> permitAllPatterns =
            Arrays.stream(PERMIT_ALL_PATTERNS)
                    .map(parser::parse)
                    .toList();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (PathPattern pattern : permitAllPatterns) {
            if (pattern.matches(PathContainer.parsePath(uri))) {
                return true; // permitAll 경로면 필터를 적용하지 않음
            }
        }
        return false;
    }

}
