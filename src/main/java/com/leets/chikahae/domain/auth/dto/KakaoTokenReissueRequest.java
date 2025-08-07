package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
@Schema(description = "토큰 재발급 요청 DTO")
public class KakaoTokenReissueRequest {

    @Schema(description = "기존 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "유효한 Refresh Token", example = "d8s7f98sd7f9s8df7...")
    private String refreshToken;


}//class
