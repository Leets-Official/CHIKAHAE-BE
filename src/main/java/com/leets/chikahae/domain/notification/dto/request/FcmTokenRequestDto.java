package com.leets.chikahae.domain.notification.dto.request;

/**
 * FCM 토큰 등록/갱신 요청 DTO
 */
public record FcmTokenRequestDto(String fcmToken) {
	public static String toToken(FcmTokenRequestDto req) {
		return req.fcmToken();
	}
}
