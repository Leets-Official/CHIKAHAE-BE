package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;


@Schema(description = "카카오 회원가입 요청 DTO")
public record KakaoSignupRequest (


    @Schema(description = "카카오 Access Token", example = "kakao-access-token-example")
    String kakaoAccessToken,

    @Schema(description = "자녀 이름", example = "난아가")
    String name,

    @Schema(description = "자녀 닉네임", example = "치카요정2")
    String nickname,

    @Schema(description = "생년월일", example = "2017-08-25")
    LocalDate birth,

    @Schema(description = "성별 (true: 남자, false: 여자)", example = "false")
    Boolean gender,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.png")
    String profileImage

){}//class
