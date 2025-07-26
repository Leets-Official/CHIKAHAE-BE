package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.dto.KakaoCallbackResponse;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.auth.util.KakaoTokenFetcher;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.member.service.MemberService;
import com.leets.chikahae.domain.token.entity.AccountToken;
import com.leets.chikahae.domain.token.repository.AccountTokenRepository;
import com.leets.chikahae.domain.token.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.leets.chikahae.global.response.ErrorCode.FORBIDDEN;

@Tag(name = "Auth", description = "카카오톡 콜백 함수")
@RestController
@RequestMapping("/login/kakao")
@RequiredArgsConstructor
public class KakaoTestController {


    private final KakaoTokenFetcher fetcher;
    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final AccountTokenRepository accountTokenRepository;


    /**
     * 인가 코드(code)를 받아 access token JSON을 반환하는 테스트용 API
     * 사용 예시: GET /login/kakao/callback
     * // 여기서 부모 정보를 반환
     */
    @GetMapping("/callback")
    public ResponseEntity<KakaoCallbackResponse> getToken(@RequestParam String code) {

        // 1. 카카오 access token 발급
        String kakaoAccessToken = fetcher.getAccessToken(code);

        // 2. 카카오 유저 정보 조회
        KakaoUserInfo user = kakaoApiClient.getUserInfo(kakaoAccessToken);
        String kakaoId = String.valueOf(user.getId());
        String nickname = user.getKakaoAccount().getProfile().getNickname();

        // 3. 회원 조회
        Member member = memberService.findByKakaoId(kakaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보 없음"));

        // 4. 서비스 자체 JWT 토큰 발급
        String serviceAccessToken = tokenService.issueAccessToken(member);
        String serviceRefreshToken = tokenService.issueRefreshToken(member);

        // 5. 응답 반환
        return ResponseEntity
                .ok()
                .header("Authorization", "Bearer " + serviceAccessToken)
                .header("Refresh-Token", serviceRefreshToken)
                .build();

    }


}//class
