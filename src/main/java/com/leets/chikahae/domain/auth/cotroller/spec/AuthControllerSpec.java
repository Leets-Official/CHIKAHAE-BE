package com.leets.chikahae.domain.auth.cotroller.spec;

import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public interface AuthControllerSpec {

    @Operation(
            summary = "카카오 회원가입",
            description = "카카오 access token으로 보호자 정보를 가져온 뒤 자녀(member)를 회원가입 처리합니다."

    )
    ResponseEntity<ApiResponse<SignupResponse>> signupKakao(
            @Parameter(description = "카카오 회원가입 요청 정보")
            KakaoSignupRequest request,
            HttpServletRequest servletRequest
    );

}//interface
