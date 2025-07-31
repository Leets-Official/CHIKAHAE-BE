package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.dto.KakaoCallbackResponse;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.auth.util.KakaoTokenFetcher;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.member.service.MemberService;
import com.leets.chikahae.domain.token.repository.AccountTokenRepository;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Tag(name = "Auth", description = "카카오톡 콜백 함수")
@RestController
@RequestMapping("/login/kakao")
@RequiredArgsConstructor
@Slf4j
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
    public ApiResponse<KakaoCallbackResponse> getToken(@RequestParam String code) {

        // 1. 카카오 access token 발급
        TokenResponse tokenResponse = fetcher.getTokenResponse(code);
        String kakaoAccessToken = tokenResponse.getAccessToken();
        String kakaoRefreshToken = tokenResponse.getRefreshToken();
        log.info("카카오 access token: {}", kakaoAccessToken);
        log.info("카카오 refresh token: {}", kakaoRefreshToken);

        // 2. 카카오 유저 정보 조회
        KakaoUserInfo user = kakaoApiClient.getUserInfo(kakaoAccessToken);
        String kakaoId = String.valueOf(user.getId());
        String nickname = user.getKakaoAccount().getProfile().getNickname();
        log.info("카카오 ID: {}", kakaoId);
        log.info("닉네임: {}", nickname);

        Optional<Member> optionalMember = memberService.findByKakaoId(kakaoId);
        Member member = optionalMember.orElse(null);

        log.info("카카오 ID: " + kakaoId +
                " 멤버 ID: " + (member != null ? member.getMemberId() : "null") +
                " 카카오 access: " + kakaoAccessToken +
                " 카카오 refresh: " + kakaoRefreshToken +
                " 닉네임: " + nickname);

// 응답을 member가 null인 경우도 허용하도록 변경
        return ApiResponse.ok(
                KakaoCallbackResponse.of(
                        member != null ? member.getMemberId() : null,
                        kakaoAccessToken,
                        kakaoRefreshToken,
                        nickname
                )
        );
    }


}//class
