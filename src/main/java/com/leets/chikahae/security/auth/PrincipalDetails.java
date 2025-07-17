package com.leets.chikahae.security.auth;

import com.leets.chikahae.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *  멤버의 정보를 담고 있는 클래스
 */
@Getter
@Builder
public class PrincipalDetails implements Authentication {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated = true;

    public static PrincipalDetails of(
            Member member,
            Collection<? extends GrantedAuthority> authorities
    ) {
        return new PrincipalDetails(member, authorities);
    }

    // 생성자
    @Builder
    public PrincipalDetails(Member member, Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
        this.member = member;
        this.authorities = authorities;
        this.authenticated = authenticated;
    }

    // 생성자
    public PrincipalDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
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
        return member;  // Member 객체 반환
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
        return member != null ? member.getNickname(): "";
    }

    public Long getId() {
        return member != null ? member.getMemberId(): null;
    }
}
