package com.leets.chikahae.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "카카오 회원가입 요청 DTO")
public class KakaoSignupRequest {

    @Schema(description = "카카오 Access Token", example = "kakao-access-token-example")
    private String kakaoAccessToken;

    @Schema(description = "자녀 닉네임", example = "나는치카요정")
    private String nickname;        // 자녀 닉네임

    @Schema(description = "생년월일", example = "2017-08-25")
    private LocalDate birth;        // 생년월일

    @Schema(description = "성별 (male: 남자, female: 여자, other: 기타)", example = "female")
    private String gender;

    @Schema(description = "자녀 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.png")
    private String profileImage;    // 프로필 이미지 (nullable)

    @Schema(description = "카톡 로그인에서 받아온 부모 이름", example = "홍길동")
    private String parentName;      // 카톡 로그인에서 받아온 부모 이름

    @Schema(description = "부모 성별 (male: 남자, female: 여자, other: 기타)", example = "male")
    private String parentGender; // ✅ Boolean → String        

    @Schema(description = "부모 전화번호", example = "010-1111-2222")
    private String parentPhoneNumber; // 부모 번호
    
    @Schema(description = "부모 생년월일", example = "1987-08-25")
    private LocalDate parentBirth;        // 생년월일


}//class
