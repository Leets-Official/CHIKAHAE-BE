package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카카오 회원가입 응답 DTO")
public class SignupResponse {

    @Schema(description = "회원 ID", example = "123")
    private Long memberId;

    @Schema(description = "닉네임", example = "초코송이")
    private String nickname;

    @Schema(description = "Access Token", example = "빌드 터미널에 있는 토큰을 사용하세요")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "빌드 터미널에 있는 토큰을 사용하세요")
    private String refreshToken;


}//class
