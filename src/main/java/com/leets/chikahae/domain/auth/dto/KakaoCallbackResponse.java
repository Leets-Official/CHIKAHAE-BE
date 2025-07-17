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

    @Schema(description = "부모 이름", example = "홍길동")
    private String nickname;
}
