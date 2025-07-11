package com.leets.chikahae.domain.auth.service;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
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
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
        String kakaoId = String.valueOf(kakaoInfo.getId());
//        String email = kakaoInfo.getKakaoAccount().getEmail();
        String email = (kakaoInfo.getKakaoAccount().getEmail() != null)
                ? kakaoInfo.getKakaoAccount().getEmail()
                : "no-email-" + kakaoId + "@kakao.local";

        String parentName = kakaoInfo.getKakaoAccount().getProfile().getNickname();

        Parent parent = parentService.saveOrFind(kakaoId, email, parentName);

        // ✅ 여기서 호출만 하고
        Member member = memberService.registerChild(
                parent.getId(),
                request.getName(),
                request.getNickname(),
                request.getBirth(),
                request.getGender(),
                request.getProfileImage()
        );

        String accessToken = tokenService.issueAccessToken(member.getId(), ipAddress, userAgent);
        String refreshToken = tokenService.issueRefreshToken(member.getId());

        return new SignupResponse(accessToken, refreshToken);
    }

}//class

