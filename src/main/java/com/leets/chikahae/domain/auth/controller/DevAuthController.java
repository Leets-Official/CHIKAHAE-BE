package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import com.leets.chikahae.security.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Auth", description = "개발자용 로그인 API")
@RestController
@RequiredArgsConstructor
public class DevAuthController {

    private final ParentRepository parentRepository;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @PostMapping("api/dev-login")
    public ApiResponse<String> devLogin() {
        Member member = Member.of(
                1L,                                  // parentId (DB에 존재하는 값인지 확인)
                "개발자",
                LocalDate.of(2021, 7, 14),
                true,
                "https://example.com/profile.jpg"
        );

        Member savedMember = memberRepository.save(member);
        System.out.println("Saved Member: " + savedMember);

        Long memberId = savedMember.getId();
        System.out.println("Member ID: " + memberId);

        if (memberId == null) {
            throw new RuntimeException("Member 저장 후 ID가 생성되지 않았습니다.");
        }

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        PrincipalDetails principalDetails = PrincipalDetails.of(savedMember, authorities);

        SecurityUtil.setAuthentication(principalDetails);

        String accessToken = tokenService.issueAccessToken(savedMember.getId(), null, null);

        return ApiResponse.ok(accessToken);
    }


    @GetMapping("api/protected")
    public String protectedApi(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "인증이 필요합니다. (principalDetails is null)";
        }
        String nickname = principalDetails.getName();
        Long memberId = principalDetails.getId();

        return String.format("인증 성공! 사용자 ID: %d, 닉네임: %s", memberId, nickname);

    }
}
