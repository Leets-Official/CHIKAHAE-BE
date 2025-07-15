package com.leets.chikahae.domain.auth.cotroller;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.util.PrincipalDetails;
import com.leets.chikahae.security.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auth", description = "개발자용 로그인 API")
@RestController
@RequiredArgsConstructor
public class DevAuthController {

    private final ParentRepository parentRepository;
    private final TokenService tokenService;

    @PostMapping("/dev-login")
    public ApiResponse<String> devLogin() {
        // 1. 개발자용 Parent 생성
        Parent parent = Parent.of(null, "devkakaoId", "dev@gmail.com", "개발자계정");
        parentRepository.save(parent);  // ID는 DB가 AUTO_INCREMENT로 생성

        // 2. 카카오 사용자 정보 생성
        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.of(parent.getId(), parent.getEmail(), parent.getName());

        // 3. 권한 부여 및 PrincipalDetails 생성
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
        PrincipalDetails principalDetails = PrincipalDetails.of(kakaoUserInfo, authorities);

        // 4. 인증 정보 등록
        SecurityUtil.setAuthentication(principalDetails);

        // 5. AccessToken 발급 및 반환
        String accessToken = tokenService.issueAccessToken(parent.getId());
        return ApiResponse.ok(accessToken);
    }

    @GetMapping("/protected")
    public String protectedApi(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "인증이 필요합니다. (principalDetails is null)";
        }
        String nickname = principalDetails.getKakaoUserInfo().getKakaoAccount().getProfile().getNickname();
        Long userId = principalDetails.getKakaoUserInfo().getId();

        return String.format("인증 성공! 사용자 ID: %d, 닉네임: %s", userId, nickname);

    }
}
