package com.leets.chikahae.domain.auth.util;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class KakaoApiClient {

    private final WebClient webClient = WebClient.create("https://kapi.kakao.com");

    public KakaoUserInfo getUserInfo(String accessToken) {
        log.info("🐾 전달된 accessToken: {}", accessToken);  // 로그 추가
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("AccessToken이 null이거나 비어 있습니다.");
        }

        try {
            return webClient.get()
                    .uri("/v2/user/me")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))  // ✅ 여기를 변경
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        log.error("❗ 카카오 API 호출 실패. 응답 바디: {}", body);
                                        return reactor.core.publisher.Mono.error(
                                                new ResponseStatusException(HttpStatus.UNAUTHORIZED,"카카오 API 에러:" + body)
                                        );
                                    })
                    )
                    .bodyToMono(KakaoUserInfo.class)
                    .block();
        } catch (Exception e) {
            log.warn("카카오 사용자 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }



}
