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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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

        //카카오 사용자 정보 호출
        KakaoUserInfo kakaoInfo = kakaoApiClient.getUserInfo(request.getKakaoAccessToken());
        if (kakaoInfo == null) {
            throw new RuntimeException("카카오 사용자 정보를 가져오지 못했습니다.");
        }
        log.info("✅ 카카오 사용자 정보 조회 성공: kakaoId = {}", kakaoInfo.getId());
        String kakaoId = String.valueOf(kakaoInfo.getId());

        //카카오 계정 정보 체크
        KakaoUserInfo.KakaoAccount account = kakaoInfo.getKakaoAccount();
        if (account == null || account.getProfile() == null) {
            throw new RuntimeException("카카오 계정 정보가 누락되어 있습니다.");
        }
        log.info("📧 email = {}", account.getEmail());
        log.info("👤 nickname = {}", account.getProfile().getNickname());
        String email = (account.getEmail() != null)
                ? account.getEmail()
                : "no-email-" + kakaoId + "@kakao.local";


        // 부모 정보: 만 14세 미만일 경우에만 저장
        Long parentId = null;
        if (isUnder14(request.getBirth())) {
            log.info("🧒 만 14세 미만이므로 부모 정보 저장 시도");
            Parent parent = parentService.saveOrFind(
                    kakaoId,
                    email,
                    request.getParentName(),
                    request.getParentGender(),
                    request.getParentPhoneNumber(),
                    request.getParentBirth()

            );
            parentId = parent.getId();
        }


        Member member = memberService.registerMember(
                parentId,
                kakaoId,
                request.getNickname(),
                email,
                request.getBirth(),
                request.getGender()
        );


        String accessToken = tokenService.issueAccessToken(member, ipAddress, userAgent);
        String refreshToken = tokenService.issueRefreshToken(member);
        
        // 카카오 accessToken을 저장해야 회원탈퇴할때 사용가능
        tokenService.saveKakaoTokens(member,request.getKakaoAccessToken());

        // 인증 정보 주입
        PrincipalDetails principalDetails = new PrincipalDetails(
                member, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityUtil.setAuthentication(principalDetails);

        log.info("🎉 회원가입 완료: memberId = {}, nickname = {}", member.getId(), member.getNickname());



        return new SignupResponse(
                member.getNickname(),
                accessToken,
                refreshToken
        );


    }


    //회원탈퇴
    @Transactional
    public void withdraw(Member member) {

        tokenService.deleteByMemberId(member.getId());
        memberService.deleteMember(member.getId());

        String accessToken = tokenService.getKakaoAccessToken(member.getKakaoId());
        kakaoApiClient.unlink(accessToken);
    }

    //로그아웃
    // RefreshToken을 기준으로 DB에서 삭제
    public void logout(String refreshToken) {
        tokenService.logoutByRefreshToken(refreshToken);
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
