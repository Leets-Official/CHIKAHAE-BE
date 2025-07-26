package com.leets.chikahae.domain.auth.controller;
import com.leets.chikahae.domain.auth.dto.KakaoLoginRequest;
import com.leets.chikahae.domain.auth.dto.LoginResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카카오 로그인 전용 컨트롤러
 */
@RestController
@RequestMapping("api/login/kakao")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인 API")
public class LoginController {

    private final AuthService authService;

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 access token으로 이미 가입된 사용자를 인증하고 새로운 JWT를 발급합니다."
    )
    @PostMapping
    public ResponseEntity<Void> loginKakao(
            @RequestBody KakaoLoginRequest request,
            HttpServletRequest servletRequest
    ) {
        String ip = servletRequest.getRemoteAddr();
        String ua = servletRequest.getHeader("User-Agent");

        LoginResponse result = authService.login(request, ip, ua);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", "Bearer " + result.getAccessToken())
                .header("Refresh-Token", result.getRefreshToken())
                .body(null); // ✅ ResponseEntity<Void> requires explicit null body
    }











}//class
