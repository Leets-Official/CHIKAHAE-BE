
package com.leets.chikahae.domain.auth.util;

import com.leets.chikahae.domain.auth.dto.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoTokenFetcher {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build();

    public String getAccessToken(String code) {
        TokenResponse response = webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "8091dd6876b5a059fcdaa26661ea384e")
                        .with("redirect_uri", "http://localhost:8080/login/kakao/callback")
                        .with("code", code)
                        .with("client_secret", "d5rRncLu76hVHZMaFtjZ3kB7XE3zRwvW"))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if (response == null || response.getAccessToken() == null) {
            throw new RuntimeException("AccessToken 파싱 실패 (response == null 또는 토큰 없음)");
        }

        log.info("🔑 토큰 응답: {}", response);
        log.info("✅ 카카오 Access Token 발급 완료: {}", response.getAccessToken());
        log.info("🔍 카카오 TokenResponse: {}", response);
        log.info("✅ 카카오 Access Token 발급 완료: {}", response.getAccessToken());
        return response.getAccessToken();

    }









}//class
