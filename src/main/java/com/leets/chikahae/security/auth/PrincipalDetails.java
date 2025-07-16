package com.leets.chikahae.security.auth;

import com.leets.chikahae.domain.auth.dto.KakaoUserInfo;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 카카오 로그인 시 사용자의 정보를 담고 있는 클래스
 */
@Getter
public class PrincipalDetails implements Authentication {

    private final KakaoUserInfo kakaoUserInfo;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated = true;

    // 카카오 로그인용 생성자
    public static PrincipalDetails of(
            KakaoUserInfo kakaoUserInfo,
            Collection<? extends GrantedAuthority> authorities
    ) {
        return new PrincipalDetails(kakaoUserInfo, authorities);
    }

    // 생성자
    public PrincipalDetails(KakaoUserInfo kakaoUserInfo, Collection<? extends GrantedAuthority> authorities) {
        this.kakaoUserInfo = kakaoUserInfo;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return kakaoUserInfo != null ? kakaoUserInfo.getKakaoAccount().getProfile().getNickname() : "";
    }

    public String getId() {
        return kakaoUserInfo != null ? String.valueOf(kakaoUserInfo.getId()) : "";
    }
}
