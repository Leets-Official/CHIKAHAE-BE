package com.leets.chikahae.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.chikahae.domain.notification.dto.request.FcmTokenRequestDto;
import com.leets.chikahae.domain.notification.dto.response.FcmTokenResponseDto;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.global.response.ApiResponse;

@RestController
@RequestMapping("/api/users/{memberId}/fcm-tokens")
public class FcmTokenController {

	private final FcmTokenService fcmTokenService;

	public FcmTokenController(FcmTokenService fcmTokenService) {
		this.fcmTokenService = fcmTokenService;
	}

	/**
	 * POST /api/users/{memberId}/fcm-tokens
	 * 토큰 등록
	 * 요청 예시: { "token": "FCM_DEVICE_TOKEN" }
	 */
	@PostMapping
	public ApiResponse<Void> registerToken(
		@PathVariable Long memberId,
		@RequestBody FcmTokenRequestDto request) {
		fcmTokenService.upsertToken(
			memberId,
			FcmTokenRequestDto.toToken(request)
		);
		return ApiResponse.ok(null);
	}

	/**
	 * DELETE /api/users/{memberId}/fcm-tokens/{token}
	 * 토큰 삭제
	 */
	@DeleteMapping("/{token}")
	public ApiResponse<Void> deleteToken(
		@PathVariable Long memberId,
		@PathVariable String token) {
		fcmTokenService.deleteToken(token);
		return ApiResponse.ok(null);
	}

	/**
	 * GET /api/users/{memberId}/fcm-tokens
	 * 토큰 조회
	 */
	@GetMapping
	public ApiResponse<List<FcmTokenResponseDto>> getTokens(
		@PathVariable Long memberId) {
		List<FcmTokenResponseDto> response = fcmTokenService.getTokens(memberId).stream()
			.map(FcmTokenResponseDto::from)
			.toList();
		return ApiResponse.ok(response);
	}
}
