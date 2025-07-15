package com.leets.chikahae.domain.notification.dto.response;

import com.leets.chikahae.domain.notification.entity.FcmToken;

/**
 * FCM 토큰 응답 DTO
 */
public record FcmTokenResponseDto(String token) {
	public static FcmTokenResponseDto from(FcmToken entity) {
		return new FcmTokenResponseDto(entity.getFcmToken());
	}
}
