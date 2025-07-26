package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.controller.spec.AuthControllerSpec;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/signup")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final AuthService authService;


    @PostMapping("/kakao")
    @Operation(
            summary = "카카오 회원가입",
            description = """
                카카오 access token으로 회원가입을 수행합니다.  
                JWT access token과 refresh token은 응답 **Header**에 포함되어 반환됩니다.
                
                - `Authorization: Bearer {access_token}`
                - `Refresh-Token: {refresh_token}`
                """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공 (헤더에 토큰 포함)",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(hidden = true))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = com.leets.chikahae.global.response.ApiResponse.class)) // 👈 DTO 명확히 구분
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "카카오 토큰이 유효하지 않음",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = com.leets.chikahae.global.response.ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<SignupResponse>> signupKakao(
            @RequestBody KakaoSignupRequest request,
            HttpServletRequest servletRequest) {

        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("USER_AGENT");
        SignupResponse response = authService.signup(request, ip, userAgent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + response.getAccessToken())
                .header("Refresh-Token", response.getRefreshToken())
                .body(null);
    }

}//class
