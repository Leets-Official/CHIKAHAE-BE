package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.controller.spec.AuthControllerSpec;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final AuthService authService;


    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<SignupResponse>> signupKakao(
            @RequestBody KakaoSignupRequest request,
            HttpServletRequest servletRequest) {

        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("USER_AGENT");
        SignupResponse response = authService.signup(request, ip, userAgent);

        return ResponseEntity
                .status(ApiResponse.ok(response).httpStatus())
                .body(ApiResponse.ok(response));
    }

}//class
