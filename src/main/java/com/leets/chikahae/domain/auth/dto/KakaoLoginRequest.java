package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카카오 로그인 요청 DTO")
public class KakaoLoginRequest {

    @Schema(description = "새로운 Access Token")
    private String accessToken;


}
