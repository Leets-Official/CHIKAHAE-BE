package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카카오 로그인 응답 DTO")
public class LoginResponse {

    @Schema(description = "회원 ID", example = "123")
    private Long memberId;

    @Schema(description = "회원 닉네임", example = "초코송이엄마")
    private String nickname;

    @Schema(description = "새로운 Access Token")
    private String accessToken;

    @Schema(description = "새로운 Refresh Token")
    private String refreshToken;
}//class
