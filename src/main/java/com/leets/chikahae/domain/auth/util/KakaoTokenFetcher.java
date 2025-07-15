package com.leets.chikahae.domain.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KakaoTokenFetcher {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build();

//    public String getAccessToken(String code) {
//        return webClient.post()
//                .uri("/oauth/token")
//                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
//                        .with("client_id", "8091dd6876b5a059fcdaa26661ea384e")
//                        .with("redirect_uri", "http://localhost:8080/login/kakao/callback")
//                        .with("code", code))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }

    public String getAccessToken(String code) {
        return webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "8091dd6876b5a059fcdaa26661ea384e")
                        .with("client_secret", "d5rRncLu76hVHZMaFtjZ3kB7XE3zRwvW")
                        .with("redirect_uri", "http://localhost:8080/login/kakao/callback")
                        .with("code", code))
                .retrieve()
                // → 여기서 method reference 대신 람다 사용
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Kakao 인증 실패: " + body)))
                .bodyToMono(String.class)
                .doOnError(e -> log.error("🔥 Kakao 토큰 요청 중 에러 발생", e))
                .block();
    }














}//class
