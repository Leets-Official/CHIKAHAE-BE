package com.leets.chikahae.domain.auth.util;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoApiClient {

    private final WebClient webClient = WebClient.create("https://kapi.kakao.com");

    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block(); // 동기 처리

        // 예외 처리는 try-catch 혹은 onStatus로 처리 가능
    }

}
