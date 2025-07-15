package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.util.KakaoTokenFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/kakao")
@RequiredArgsConstructor
public class KakaoTestController {


    private final KakaoTokenFetcher fetcher;

    /**
     * 인가 코드(code)를 받아 access token JSON을 반환하는 테스트용 API
     * 사용 예시: GET /login/kakao/callback
     */
    @GetMapping("/callback")
    public ResponseEntity<String> getToken(@RequestParam String code) {
        String token = fetcher.getAccessToken(code);
        return ResponseEntity.ok(token);
    }




}//class
