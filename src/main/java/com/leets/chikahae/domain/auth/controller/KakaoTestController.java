package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.dto.KakaoCallbackResponse;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.auth.util.KakaoTokenFetcher;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "카카오톡 콜백 함수")
@RestController
@RequestMapping("/login/kakao")
@RequiredArgsConstructor
public class KakaoTestController {


    private final KakaoTokenFetcher fetcher;
    private final KakaoApiClient kakaoApiClient;

    /**
     * 인가 코드(code)를 받아 access token JSON을 반환하는 테스트용 API
     * 사용 예시: GET /login/kakao/callback
     * // 여기서 부모 정보를 반환
     */
    @GetMapping("/callback")
    public ResponseEntity<KakaoCallbackResponse> getToken(@RequestParam String code) {
        String token = fetcher.getAccessToken(code);

        // 부모 이름 반환
        KakaoUserInfo user = kakaoApiClient.getUserInfo(token);
        return ResponseEntity.ok(new KakaoCallbackResponse(token, user.getKakaoAccount().getProfile().getNickname())); // 3. 토큰 + 닉네임 반환
    }




}//class
