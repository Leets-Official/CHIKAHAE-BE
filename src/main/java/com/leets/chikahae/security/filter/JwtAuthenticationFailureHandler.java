package com.leets.chikahae.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
    * 인증 실패 시 호출되는 핸들러
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 로그인 필요 401 Error 발생
        CustomException exception = new CustomException(ErrorCode.INVALID_LOGIN);
        ApiResponse<Object> apiResponse = ApiResponse.fail(exception);

        // 응답 설정
        response.setStatus(apiResponse.httpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
