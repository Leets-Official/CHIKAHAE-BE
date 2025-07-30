package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.controller.spec.AuthControllerSpec;
import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/signup")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final AuthService authService;
    private final TokenService tokenService;


    //회원가입
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

   //회원탈퇴
    @Operation(
            summary = "회원탈퇴",
            description = """
            현재 사용자의 회원 정보를 삭제하고, 카카오와의 연결을 해제합니다.
    
            요청 헤더에 아래 형식의 Access Token이 포함되어야 합니다.
            - Authorization: Bearer {access_token}
            """,
            security = @SecurityRequirement(name = "JWT") // Swagger 상단 Authorize 토큰 적용
    )
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @RequestHeader("Authorization") String token) { // ✅ @Parameter 제거!
        authService.withdraw(token);
        return ResponseEntity.noContent().build();
    }

    //로그아웃
    @Operation(
            summary = "로그아웃",
            description = """
        현재 사용자의 Refresh Token을 무효화합니다.  
        서버 DB에서 해당 Refresh Token을 삭제하며, 카카오 서버와의 연결은 유지됩니다.

        요청 바디에 아래 형식의 Refresh Token이 포함되어야 합니다.
        {
            "refreshToken": "xxx.yyy.zzz"
        }
        """,
            security = @SecurityRequirement(name = "JWT")
    )
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
        authService.logout(refreshToken.replace("Bearer ", ""));
        return ResponseEntity.noContent().build();
    }

    //Access Token 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponse> reissueAccessToken(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse response = tokenService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }








}//class
