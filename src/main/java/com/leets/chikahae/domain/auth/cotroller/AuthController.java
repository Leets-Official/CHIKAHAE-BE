package com.leets.chikahae.domain.auth.cotroller;

import com.leets.chikahae.domain.auth.dto.KakaoSignupRequest;
import com.leets.chikahae.domain.auth.dto.SignupResponse;
import com.leets.chikahae.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Operation(
            summary = "카카오 회원가입",
            description = "카카오 access token으로 보호자 정보를 가져온 뒤 자녀(member)를 회원가입 처리합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "카카오 회원가입 요청 정보"
            )
    )

    @PostMapping("/kakao")
    public ResponseEntity<SignupResponse> signupKakao(
            @RequestBody KakaoSignupRequest request,
            HttpServletRequest servletRequest) {

        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("USER_AGENT");

        SignupResponse response = authService.signup(request, ip, userAgent);
        return ResponseEntity.ok(response);
    }

}//class
