package com.leets.chikahae.domain.auth.service;

import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.KakaoLoginRequest;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.dto.LoginResponse;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.service.ParentService;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.service.MemberService;
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
    private final MemberService memberService;
    private final TokenService tokenService;
    private final ParentService parentService;
    private final KakaoApiClient kakaoApiClient;

    /**
     * 카카오 회원가입 및 토큰 발급
     */
    @Transactional
    public SignupResponse signup(KakaoSignupRequest request, String ipAddress, String userAgent) {
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
        String kakaoId = String.valueOf(kakaoInfo.getId());

        //기본 이메일 처리
        String email = (kakaoInfo.getKakaoAccount().getEmail() != null)
                ? kakaoInfo.getKakaoAccount().getEmail()
                : "no-email-" + kakaoId + "@kakao.local";

//        Parent parent = parentService.saveOrFind(kakaoId, email, request.getParentName(),request.getParentGender(),request.getParentBirth());

        // 부모 정보: 만 14세 미만일 경우에만 저장
        Long parentId = null;
        if (isUnder14(request.getBirth())) {
            Parent parent = parentService.saveOrFind(
                    kakaoId,
                    email,
                    request.getParentName(),
                    request.getParentGender(),
                    request.getParentBirth()
            );
            parentId = parent.getId();
        }
        

        Member member = memberService.registerMember(
                parentId,
                kakaoId,
                request.getNickname(),
                request.getBirth(),
                request.getGender(),
                request.getProfileImage()
        );

        String accessToken = tokenService.issueAccessToken(member, ipAddress, userAgent);
        String refreshToken = tokenService.issueRefreshToken(member);

        // 인증 정보 주입
        PrincipalDetails principalDetails = new PrincipalDetails(
                member, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityUtil.setAuthentication(principalDetails);

        return new SignupResponse(
                member.getId(),
                member.getNickname(),
                accessToken,
                refreshToken
        );
    }

    /**
     * 카카오 로그인 및 토큰 재발급
     */
    @Transactional
    public LoginResponse login(KakaoLoginRequest request, String ipAddress, String userAgent) {
        KakaoUserInfo info = kakaoApiClient.getUserInfo(request.getAccessToken());
        String kakaoId = String.valueOf(info.getId());

        Member member = memberService.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("등록된 사용자가 아닙니다."));

        String newAccess = tokenService.issueAccessToken(member, ipAddress, userAgent);
        String newRefresh = tokenService.issueRefreshToken(member);

        // 인증 정보 주입
        PrincipalDetails principalDetails = new PrincipalDetails(
                member, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityUtil.setAuthentication(principalDetails);

        return new LoginResponse(
                member.getId(),
                member.getNickname(),
                newAccess,
                newRefresh
        );
    }
    
    //isUnder 유틸 함수
    private boolean isUnder14(java.time.LocalDate birth) {
        return java.time.Period.between(birth, java.time.LocalDate.now()).getYears() < 14;
    }

    
    
}//class
