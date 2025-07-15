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

        // ✅ 여기서 호출만 하고
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



}//class

