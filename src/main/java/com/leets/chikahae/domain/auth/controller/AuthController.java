package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.controller.spec.AuthControllerSpec;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.dto.TokenRequest;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerSpec {

    private final AuthService authService;
    private final TokenService tokenService;

    @Override
    public ApiResponse<SignupResponse> signupKakao(KakaoSignupRequest request, HttpServletRequest servletRequest) {
        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("USER_AGENT");
        SignupResponse response = authService.signup(request, ip, userAgent);

        return ApiResponse.ok(response);
    }

    @Override
    public ApiResponse<String> withdraw(TokenRequest refreshToken) {
        authService.withdraw(refreshToken.getRefreshToken());
        return ApiResponse.ok("회원탈퇴가 완료되었습니다.");
    }

    @Override
    public ApiResponse<String> logout(String refreshToken) {
        authService.logout(refreshToken.replace("Bearer ", ""));
        return ApiResponse.ok("로그아웃이 완료되었습니다.");
    }

    @Override
    public ApiResponse<TokenResponse> reissueAccessToken(TokenRequest request) {
        TokenResponse response = tokenService.reissueAccessToken(request.getRefreshToken());
        return ApiResponse.ok(response);
    }

}
