package com.leets.chikahae.domain.auth.service;

import com.leets.chikahae.domain.auth.dto.*;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.service.MemberService;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.service.ParentService;
import com.leets.chikahae.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final ParentService parentService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @Transactional
    public SignupResponse signup(KakaoSignupRequest request, String ipAddress, String userAgent) {
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.kakaoAccessToken());
        String kakaoId = String.valueOf(kakaoInfo.getId());
        String email = (kakaoInfo.getKakaoAccount().getEmail() != null)
                ? kakaoInfo.getKakaoAccount().getEmail()
                : "no-email-" + kakaoId + "@kakao.local";

        String parentName = kakaoInfo.getKakaoAccount().getProfile().getNickname();

        Parent parent = parentService.saveOrFind(kakaoId, email, parentName);

        Member member = memberService.registerChild(
                parent.getId(),
                request.name(),
                request.nickname(),
                request.birth(),
                request.gender(),
                request.profileImage()
        );

        String accessToken = tokenService.issueAccessToken(member.getId(), ipAddress, userAgent);
        String refreshToken = tokenService.issueRefreshToken(member.getId());

        // 수정 후
        return new SignupResponse(
                member.getId(),
                member.getNickname(),
                accessToken,
                refreshToken
        );
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
        String newAccess  = tokenService.issueAccessToken(member.getId(), ipAddress, userAgent);
        String newRefresh = tokenService.issueRefreshToken(member.getId());

        // 4) 응답 DTO 반환
        return new LoginResponse(
                member.getId(),
                member.getNickname(),
                newAccess,
                newRefresh
        );
    }

   

//     @Transactional(readOnly = true)
//     public SignupResponse kakaoLogin(KakaoSignupRequest request) {
//         KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
//         String kakaoId = String.valueOf(kakaoInfo.getId());

//         Parent parent = parentService.findByKakaoId(kakaoId)
//                 .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 부모가 없습니다."));

//         Member member = memberService.findFirstChildByParentId(parent.getId())
//                 .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 자녀가 없습니다."));

//         return authenticateAndIssueTokens(kakaoInfo, member);
//     }

//     // 공통 토큰 발급 로직
//     private SignupResponse authenticateAndIssueTokens(KakaoUserInfo kakaoInfo, Member member) {
//         PrincipalDetails principalDetails = new PrincipalDetails(
//                 kakaoInfo, List.of(new SimpleGrantedAuthority("ROLE_USER"))
//         );
//         SecurityUtil.setAuthentication(principalDetails);

//         String accessToken = tokenService.issueAccessToken(member.getId());
//         String refreshToken = tokenService.issueRefreshToken(member.getId());

//         return new SignupResponse(accessToken, refreshToken);
    }
}//class

