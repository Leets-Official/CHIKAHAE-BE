package com.leets.chikahae.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private Long id;  // kakao_id

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;


    @Builder
    public KakaoUserInfo(Long id, KakaoAccount kakaoAccount) {
        this.id = id;
        this.kakaoAccount = kakaoAccount;
    }

    @Getter
    @Builder
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @Builder
        public static class Profile {
            private String nickname;
        }
    }

    public static KakaoUserInfo of(Long id, String email, String nickname) {
        return KakaoUserInfo.builder()
                .id(id)
                .kakaoAccount(
                        KakaoAccount.builder()
                                .email(email)
                                .profile(
                                        KakaoAccount.Profile.builder()
                                                .nickname(nickname)
                                                .build()
                                )
                                .build()
                )
                .build();
    }

}//class
