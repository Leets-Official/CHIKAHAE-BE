package com.leets.chikahae.domain.notification.controller;

import com.leets.chikahae.domain.notification.controller.spec.NotificationSlotControllerSpec;
import com.leets.chikahae.domain.notification.dto.request.NotificationSlotToggleRequestDto;
import com.leets.chikahae.domain.notification.dto.request.NotificationSlotUpdateTimeRequestDto;
import com.leets.chikahae.domain.notification.dto.response.NotificationSlotResponseDto;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationSlotController implements NotificationSlotControllerSpec {

	private final NotificationSlotService notificationSlotService;

	@Override
	public ApiResponse<List<NotificationSlotResponseDto>> getSlots(PrincipalDetails user) {
		List<NotificationSlotResponseDto> response = notificationSlotService.getSlots(user.getId()).stream()
				.map(NotificationSlotResponseDto::from)
				.toList();
		return ApiResponse.ok(response);
	}

	@Override
	public ApiResponse<Void> updateTime(PrincipalDetails user, SlotType slotType, NotificationSlotUpdateTimeRequestDto request) {
		notificationSlotService.updateSlotTime(
				user.getId(),
				slotType,
				NotificationSlotUpdateTimeRequestDto.toLocalTime(request),
				java.time.ZoneId.systemDefault()
		);
		return ApiResponse.ok(null);
	}

	@Override
	public ApiResponse<Void> toggleSlot(PrincipalDetails user, SlotType slotType, NotificationSlotToggleRequestDto request) {
		notificationSlotService.toggleSlot(
				user.getId(),
				slotType,
				NotificationSlotToggleRequestDto.toEnabled(request)
		);
		return ApiResponse.ok(null);
	}

	@Override
	public ApiResponse<Void> toggleAllSlots(PrincipalDetails user, NotificationSlotToggleRequestDto request) {
		boolean enabled = NotificationSlotToggleRequestDto.toEnabled(request);
		notificationSlotService.toggleAllSlots(user.getId(), enabled);
		return ApiResponse.ok(null);
	}
}
