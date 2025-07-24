package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import com.leets.chikahae.domain.point.entity.Point;
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
import java.time.ZoneId;
import java.util.List;

@Tag(name = "Auth", description = "개발자용 로그인 API")
@RestController
@RequiredArgsConstructor
public class DevAuthController {

    private final ParentRepository parentRepository;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final NotificationSlotService notificationSlotService;

    @PostMapping("api/dev-login")
    public ApiResponse<String> devLogin() {
        Parent parent=Parent.builder()
                .kakaoId("dev-kakao-id")
                .email("dev@example.com")
                .name("테스트 부모")
                .gender(true)
                .birth(LocalDate.of(1980, 1, 1))
                .isDelete("N") // 기본값 설정
                .build();
        parentRepository.save(parent); // 부모 엔티티 저장

        Member member = Member.of(
                1L,                                  // parentId (DB에 존재하는 값인지 확인)
                "개발자",
                LocalDate.of(2021, 7, 14),
                true,
                "https://example.com/profile.jpg"
        );

        Member savedMember = memberRepository.save(member);
        System.out.println("Saved Member: " + savedMember);

        //  Point 엔티티 생성 및 저장
        Point point = Point.of(savedMember);
        savedMember.setPoint(point); // 연관 관계 편의 메서드

        notificationSlotService.createDefaultSlots(member, ZoneId.of("Asia/Seoul"));


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
