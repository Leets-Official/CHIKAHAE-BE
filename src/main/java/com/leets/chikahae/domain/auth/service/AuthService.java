package com.leets.chikahae.domain.auth.service;

import com.leets.chikahae.domain.auth.dto.*;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.service.MemberService;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.service.ParentService;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.security.auth.PrincipalDetails;
import com.leets.chikahae.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final KakaoApiClient kakaoApiClient;
    private final ParentService parentService;
    private final MemberService memberService;
    private final NotificationSlotService notificationSlotService;

    @Transactional
    public SignupResponse signup(KakaoSignupRequest request, String ipAddress, String userAgent) {
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
        String kakaoId = String.valueOf(kakaoInfo.getId());

        // 기본값 설정
        String email = (kakaoInfo.getKakaoAccount() != null && kakaoInfo.getKakaoAccount().getEmail() != null)
                ? kakaoInfo.getKakaoAccount().getEmail()
                : "no-email-" + kakaoId + "@kakao.local";

        String parentName = (kakaoInfo.getKakaoAccount() != null && kakaoInfo.getKakaoAccount().getProfile() != null)
                ? kakaoInfo.getKakaoAccount().getProfile().getNickname()
                : "카카오사용자";

        // 부모 저장 또는 조회
        Parent parent = parentService.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카카오 ID의 부모 정보가 없습니다."));

        Member member = memberService.registerChild(
                parent.getParentId(),
                request.getName(),
                request.getNickname(),
                request.getBirth(),
                request.getGender(),
                request.getProfileImage()
        );
        return authenticateAndIssueTokens(kakaoInfo, member);
    }

    @Transactional
    public LoginResponse login(KakaoLoginRequest request, String ipAddress, String userAgent) {
        // 1) 카카오 유저 정보 조회
        KakaoUserInfo info = kakaoApiClient.getUserInfo(request.getAccessToken());
        String kakaoId = String.valueOf(info.getId());

        // 2) 가입된 Member 조회
        Member member = memberService.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("등록된 사용자가 아닙니다."));

        // 3) 토큰 재발급
        String newAccess = tokenService.issueAccessToken(member.getId());
        String newRefresh = tokenService.issueRefreshToken(member.getId());

        // 4) 응답 DTO 반환
        return new LoginResponse(
                member.getId(),
                member.getNickname(),
                newAccess,
                newRefresh
        );
    }
//    @Transactional(readOnly = true)
//    public SignupResponse kakaoLogin(KakaoSignupRequest request) {
//        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
//        String kakaoId = String.valueOf(kakaoInfo.getId());
//
//        Parent parent = parentService.findByKakaoId(kakaoId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 부모가 없습니다."));
//
//        Member member = memberService.findByKakaoId(parent.getKakaoId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 자녀가 없습니다."));
//
//        return authenticateAndIssueTokens(kakaoInfo, member);
//    }

    // 공통 토큰 발급 로직
    private SignupResponse authenticateAndIssueTokens(KakaoUserInfo kakaoInfo, Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(
                kakaoInfo, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityUtil.setAuthentication(principalDetails);

        String accessToken = tokenService.issueAccessToken(member.getId());
        String refreshToken = tokenService.issueRefreshToken(member.getId());
        return new SignupResponse(member.getMemberId(), member.getNickname(), accessToken, refreshToken);
    }

}
//    @Transactional
//    public LoginResponse login(KakaoLoginRequest request, String ipAddress, String userAgent) {
//        // 1) 카카오 유저 정보 조회
//        KakaoUserInfo info = kakaoApiClient.getUserInfo(request.getAccessToken());
//        String kakaoId = String.valueOf(info.getId());
//
//        // 2) 가입된 Member 조회
//        Member member = memberService.findByKakaoId(kakaoId)
//                .orElseThrow(() -> new RuntimeException("등록된 사용자가 아닙니다."));
//
//
//        // 3) 토큰 재발급
//        String newAccess  = tokenService.issueAccessToken(member.getId());
//        String newRefresh = tokenService.issueRefreshToken(member.getId());
//
/// /        기본 슬롯 생성 (07-15 문석준)
//        notificationSlotService.createDefaultSlots(member, ZoneId.of("Asia/Seoul"));
//
//        // 4) 응답 DTO 반환
//        return new LoginResponse(
//                member.getId(),
//                member.getNickname(),
//                newAccess,
//                newRefresh
//        );
//    }


//            @Transactional
//            public LoginResponse login (KakaoLoginRequest request, String ipAddress, String userAgent){
//                // 1) 카카오 유저 정보 조회
//                KakaoUserInfo info = kakaoApiClient.getUserInfo(request.getAccessToken());
//                String kakaoId = String.valueOf(info.getId());
//
//                // 2) 가입된 Member 조회
//                Member member = memberService.findByKakaoId(kakaoId)
//                        .orElseThrow(() -> new RuntimeException("등록된 사용자가 아닙니다."));
//
//
//                // 3) 토큰 재발급
//                String newAccess = tokenService.issueAccessToken(member.getId(), ipAddress, userAgent);
//                String newRefresh = tokenService.issueRefreshToken(member.getId());
//
//                // 4) 응답 DTO 반환
//                return new LoginResponse(
//                        member.getId(),
//                        member.getNickname(),
//                        newAccess,
//                        newRefresh
//                );
//            }

// 공통 토큰 발급 로직
//     private SignupResponse authenticateAndIssueTokens(KakaoUserInfo kakaoInfo, Member member) {
//         PrincipalDetails principalDetails = new PrincipalDetails(
//                 kakaoInfo, List.of(new SimpleGrantedAuthority("ROLE_USER"))
//         );
//         SecurityUtil.setAuthentication(principalDetails);
//
//         String accessToken = tokenService.issueAccessToken(member.getId());
//         String refreshToken = tokenService.issueRefreshToken(member.getId());
//
//         return new SignupResponse(accessToken, refreshToken);
//     }
//        }//class
