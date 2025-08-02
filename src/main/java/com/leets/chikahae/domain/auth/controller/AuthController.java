package com.leets.chikahae.domain.auth.controller;

import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.dto.TokenRequest;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import com.leets.chikahae.domain.token.service.TokenService;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/signup")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;


    //회원가입
    @PostMapping("/kakao")
    public ApiResponse<SignupResponse> signupKakao(
            @RequestBody KakaoSignupRequest request,
            HttpServletRequest servletRequest) {

        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("USER_AGENT");
        SignupResponse response = authService.signup(request, ip, userAgent);

        return ApiResponse.ok(response);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .header("Authorization", "Bearer " + response.getAccessToken())
//                .header("Refresh-Token", response.getRefreshToken())
//                .body(null);
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
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody TokenRequest refreshToken) {
        authService.withdraw(refreshToken.getRefreshToken());
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



    @Operation(
            summary = "Access Token 재발급",
            description = """
        저장된 Refresh Token을 기반으로 Access Token을 새로 발급합니다.  
        요청 바디에 아래 형식의 JSON을 포함시켜야 합니다.

        {
            "refreshToken": "xxx.yyy.zzz"
        }
        """,
            security = @SecurityRequirement(name = "JWT")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Access Token 발급 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 Refresh Token",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema =
                    @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class))
            )
    })
    // Access Token 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponse> reissueAccessToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }









}//class
