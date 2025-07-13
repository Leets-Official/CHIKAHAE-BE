package com.leets.chikahae.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * JWT 인증 과정에서 발생하는 예외를 나타냄
 * 인증 실패 시 발생하며, 메시지를 통해 상세한 오류 정보를 제공
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String s) {
        super(s);
    }
}
