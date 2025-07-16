package com.leets.chikahae.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.leets.chikahae.domain.notification.dto.request.NotificationSlotToggleRequestDto;
import com.leets.chikahae.domain.notification.dto.request.NotificationSlotUpdateTimeRequestDto;
import com.leets.chikahae.domain.notification.dto.response.NotificationSlotResponseDto;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification Slots", description = "알림 슬롯 조회·수정 API")
@RestController
@RequestMapping("/api/notifications/slots")
public class NotificationSlotController {

	private final NotificationSlotService notificationSlotService;

	public NotificationSlotController(NotificationSlotService notificationSlotService) {
		this.notificationSlotService = notificationSlotService;
	}

	/**
	 * GET /api/notifications/slots
	 * 알림 조회
	 */
	@Operation(
		summary     = "알림 슬롯 조회",
		description = "현재 사용자의 모든 알림 슬롯 설정을 조회합니다."
	)
	@GetMapping
	public ApiResponse<List<NotificationSlotResponseDto>> getSlots(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		Long memberId = principalDetails.getMember().getMemberId();
		List<NotificationSlotResponseDto> response = notificationSlotService.getSlots(memberId).stream()
			.map(NotificationSlotResponseDto::from)
			.toList();
		return ApiResponse.ok(response);
	}
	//MORNING, LUNCH, EVENING 중 하나를 slotType 자리에 넣어 주면 됩니다.

	/**
	 * PATCH /api/notifications/slots/{slotType}/time
	 * 슬롯 시간대 변경
	 * 요청 예시: { "sendTime": "HH:mm:ss" }
	 */
	@Operation(
		summary     = "슬롯 시간대 변경",
		description = "지정한 슬롯의 발송 시간을 업데이트합니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required    = true,
			description = "변경할 발송 시간 정보",
			content     = @Content(
				mediaType = "application/json",
				schema    = @Schema(implementation = NotificationSlotUpdateTimeRequestDto.class),
				examples  = @ExampleObject(
					name  = "요청 예시",
					value = "{\"sendTime\": \"HH:mm:ss\"}"
				)
			)
		)
	)
	@PatchMapping("/{slotType}/time")
	public ApiResponse<Void> updateTime(
		@AuthenticationPrincipal PrincipalDetails principalDetails,
		@PathVariable SlotType slotType,
		@RequestBody NotificationSlotUpdateTimeRequestDto request) {

		Long memberId = principalDetails.getMember().getMemberId();
		notificationSlotService.updateSlotTime(
			memberId,
			slotType,
			NotificationSlotUpdateTimeRequestDto.toLocalTime(request),
			java.time.ZoneId.systemDefault()
		);
		return ApiResponse.ok(null);
	}


	/**
	 * PATCH /api/notifications/slots/{slotType}/enabled
	 * 슬롯 on/off
	 * 요청 예시: { "enabled": true }
	 */
	@Operation(
		summary     = "슬롯 on/off 토글",
		description = "지정한 슬롯의 활성화 상태를 변경합니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required    = true,
			description = "토글할 활성화 상태 정보",
			content     = @Content(
				mediaType = "application/json",
				schema    = @Schema(implementation = NotificationSlotToggleRequestDto.class),
				examples  = @ExampleObject(
					name  = "요청 예시",
					value = "{\"enabled\": true}"
				)
			)
		)
	)
	@PatchMapping("/{slotType}/enabled")
	public ApiResponse<Void> toggleSlot(
		@AuthenticationPrincipal PrincipalDetails principalDetails,
		@PathVariable SlotType slotType,
		@RequestBody NotificationSlotToggleRequestDto request) {

		Long memberId = principalDetails.getMember().getMemberId();
		notificationSlotService.toggleSlot(
			memberId,
			slotType,
			NotificationSlotToggleRequestDto.toEnabled(request)
		);
		return ApiResponse.ok(null);
	}

}
