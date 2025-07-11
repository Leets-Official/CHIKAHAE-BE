package com.leets.chikahae.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.chikahae.domain.notification.dto.request.NotificationSlotToggleRequestDto;
import com.leets.chikahae.domain.notification.dto.request.NotificationSlotUpdateTimeRequestDto;
import com.leets.chikahae.domain.notification.dto.response.NotificationSlotResponseDto;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.global.response.ApiResponse;

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
	@GetMapping
	public ApiResponse<List<NotificationSlotResponseDto>> getSlots(@AuthenticationPrincipal Member member) {
		List<NotificationSlotResponseDto> response = notificationSlotService.getSlots(member).stream()
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
	@PatchMapping("/{slotType}/time")
	public ApiResponse<Void> updateTime(
		@AuthenticationPrincipal Member member,
		@PathVariable SlotType slotType,
		@RequestBody NotificationSlotUpdateTimeRequestDto request) {
		notificationSlotService.updateSlotTime(
			member,
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
	@PatchMapping("/{slotType}/enabled")
	public ApiResponse<Void> toggleSlot(
		@AuthenticationPrincipal Member member,
		@PathVariable SlotType slotType,
		@RequestBody NotificationSlotToggleRequestDto request) {
		notificationSlotService.toggleSlot(
			member,
			slotType,
			NotificationSlotToggleRequestDto.toEnabled(request)
		);
		return ApiResponse.ok(null);
	}

}
