package com.leets.chikahae.domain.auth.controller.spec;

import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.dto.TokenRequest;
import com.leets.chikahae.domain.auth.dto.TokenResponse;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public interface AuthControllerSpec {

    @Operation(
            summary = "카카오 회원가입",
            description = "카카오 access token으로 보호자 정보를 가져온 뒤 자녀(member)를 회원가입 처리합니다."
    )
    @PostMapping("/api/signup/kakao")
    ApiResponse<SignupResponse> signupKakao(
            @Parameter(description = "카카오 회원가입 요청 정보")
            @RequestBody KakaoSignupRequest request,
            HttpServletRequest servletRequest
    );


    @Operation(
            summary = "회원탈퇴",
            description = """
                    현재 사용자의 회원 정보를 삭제하고, 카카오와의 연결을 해제합니다.
                    요청 헤더에 아래 형식의 Access Token이 포함되어야 합니다.
                    - Authorization: Bearer {access_token}
                    """
    )
    @PostMapping("/api/signup/withdraw")
    ApiResponse<String> withdraw(
            @Parameter(description = "Refresh Token")
            @RequestBody TokenRequest refreshToken
    );


    @Operation(
            summary = "로그아웃",
            description = """
                    현재 사용자의 Refresh Token을 무효화합니다.  
                    서버 DB에서 해당 Refresh Token을 삭제하며, 카카오 서버와의 연결은 유지됩니다.
                    
                    요청 바디에 아래 형식의 Refresh Token이 포함되어야 합니다.
                    {
                        "refreshToken": "xxx.yyy.zzz"
                    }
                    """
    )
    @DeleteMapping("/api/signup/logout")
    ApiResponse<String> logout(
            @Parameter(description = "Authorization 헤더의 Refresh Token")
            @RequestHeader("Authorization") String refreshToken
    );


    @Operation(
            summary = "Access Token 재발급",
            description = """
                    저장된 Refresh Token을 기반으로 Access Token을 새로 발급합니다.  
                    요청 바디에 아래 형식의 JSON을 포함시켜야 합니다.

                    {
                        "refreshToken": "xxx.yyy.zzz"
                    }
                    """
    )
    @PostMapping("/api/signup/auth/reissue")
    ApiResponse<TokenResponse> reissueAccessToken(
            @RequestBody TokenRequest request
    );
}
