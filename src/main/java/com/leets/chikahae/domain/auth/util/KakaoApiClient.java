package com.leets.chikahae.domain.auth.util;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoApiClient {

    private final WebClient webClient = WebClient.create("https://kapi.kakao.com");

    public KakaoUserInfo getUserInfo(String accessToken) {
        log.info("🔑 accessToken 전달됨: {}", accessToken);
        return webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block(); // 동기 처리
        // 예외 처리는 try-catch 혹은 onStatus로 처리 가능
    }


    public String getAccessToken(String code) {
        return WebClient.create("https://kauth.kakao.com")
                .post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=8091dd6876b5a059fcdaa26661ea384e" +  // ← REST API 키
                        "&redirect_uri=http://localhost:8080/login/kakao/callback" +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block()
                .getAccessToken();
    }


}
