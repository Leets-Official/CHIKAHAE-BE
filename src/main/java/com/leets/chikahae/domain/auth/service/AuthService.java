package com.leets.chikahae.domain.auth.service;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.util.KakaoApiClient;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.service.MemberService;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.service.ParentService;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.security.util.PrincipalDetails;
import com.leets.chikahae.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final ParentService parentService;
    private final MemberService memberService;
    private final TokenService tokenService;
    private final NotificationSlotService notificationSlotService;

    @Transactional
    public SignupResponse signup(KakaoSignupRequest request) {
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
        Parent parent = parentService.saveOrFind(kakaoId, email, parentName);

        // 자녀 등록
        Member member = memberService.registerChild(
                parent.getId(),
                request.getName(),
                request.getNickname(),
                request.getBirth(),
                request.getGender(),
                request.getProfileImage()
        );

        //기본 슬롯 생성 (07-15 문석준)
        notificationSlotService.createDefaultSlots(member, ZoneId.of("Asia/Seoul"));


        return authenticateAndIssueTokens(kakaoInfo, member);
    }

    @Transactional(readOnly = true)
    public SignupResponse kakaoLogin(KakaoSignupRequest request) {
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
        String kakaoId = String.valueOf(kakaoInfo.getId());

        Parent parent = parentService.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 부모가 없습니다."));

        Member member = memberService.findFirstChildByParentId(parent.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카카오 계정으로 등록된 자녀가 없습니다."));

        return authenticateAndIssueTokens(kakaoInfo, member);
    }

    // 공통 토큰 발급 로직
    private SignupResponse authenticateAndIssueTokens(KakaoUserInfo kakaoInfo, Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(
                kakaoInfo, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityUtil.setAuthentication(principalDetails);

        String accessToken = tokenService.issueAccessToken(member.getId());
        String refreshToken = tokenService.issueRefreshToken(member.getId());

        return new SignupResponse(accessToken, refreshToken);
    }
}//class

