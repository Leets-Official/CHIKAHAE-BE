package com.leets.chikahae.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 권한이 없는 사용자가 접근할 때 403 에러를 반환하는 핸들러
 * AccessDeniedException 발생 시 호출
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 권한 부족 403 Error
        CustomException exception = new CustomException(ErrorCode.FORBIDDEN);
        ApiResponse<Object> apiResponse = ApiResponse.fail(exception);

        /// response 제작
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답
        objectMapper.writeValue(response.getWriter(), apiResponse);

    }
}
