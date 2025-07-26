package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카카오 콜백 응답 DTO")
public class KakaoCallbackResponse {

    @Schema(description = "카톡 accessToken", example = "123")
    private String accessToken;

    @Schema(description = "refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5...")
    private String refreshToken;

    @Schema(description = "자녀 닉네임 (로그인 사용자)", example = "이지은")
    private String nickname;
}
