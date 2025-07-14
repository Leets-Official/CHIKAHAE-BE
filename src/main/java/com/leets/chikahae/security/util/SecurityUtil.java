package com.leets.chikahae.security.util;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * 로그인 인증 정보를 SecurityContext에 등록하는 유틸리티
 */
public class SecurityUtil {

    public static void setAuthentication(PrincipalDetails principalDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                principalDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
