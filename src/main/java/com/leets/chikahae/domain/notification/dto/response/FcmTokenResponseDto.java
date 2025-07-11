package com.leets.chikahae.domain.notification.dto.response;

import com.leets.chikahae.domain.notification.entity.FcmToken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmTokenResponseDto {

	private final String token;

	public static FcmTokenResponseDto from(FcmToken entity) {
		return new FcmTokenResponseDto(entity.getFcmToken());
	}

}
