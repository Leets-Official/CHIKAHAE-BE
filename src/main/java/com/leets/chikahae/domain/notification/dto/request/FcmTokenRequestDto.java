package com.leets.chikahae.domain.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

//FCM 토큰 등록/갱신 요청 dto
@Getter
@NoArgsConstructor
public class FcmTokenRequestDto {

	private String fcmToken;

	public static String toToken(FcmTokenRequestDto req) {
		return req.fcmToken;
	}
}
