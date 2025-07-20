package com.leets.chikahae.domain.notification.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import com.leets.chikahae.domain.notification.entity.FcmToken;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.BatchResponse;
import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.service.FcmPushService;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

@RestController
@RequestMapping("/api/notifications")
public class NotificationTestController {
	private final NotificationSlotService slotService;
	private final FcmTokenService tokenService;
	private final FcmPushService pushService;

	public NotificationTestController(NotificationSlotService slotService,
		FcmTokenService tokenService,
		FcmPushService pushService) {
		this.slotService = slotService;
		this.tokenService = tokenService;
		this.pushService = pushService;
	}

	@PostMapping("/trigger")
	public ApiResponse<TriggerResult> triggerNow(
		@AuthenticationPrincipal PrincipalDetails user
	) throws Exception {
		// 스케줄 무시: 회원의 모든 활성화된 슬롯을 가져옴.
		List<NotificationSlot> allSlots = slotService.getSlots(user.getId())
			.stream()
			.filter(NotificationSlot::isEnabled)
			.toList();

		int totalSuccess = 0, totalFailure = 0;
		for (NotificationSlot slot : allSlots) {
			List<String> tokens = tokenService.getTokens(slot.getMember().getMemberId())
				.stream()
				.map(FcmToken::getFcmToken)
				.toList();

			if (!tokens.isEmpty()) {
				var resp = pushService.sendMulticast(tokens, slot.getTitle(), slot.getMessage());
				totalSuccess += resp.getSuccessCount();
				totalFailure += resp.getFailureCount();
			}
		}

		TriggerResult result = new TriggerResult(allSlots.size(), totalSuccess, totalFailure);
		return ApiResponse.ok(result);
	}

	public static class TriggerResult {
		private final int slotCount;
		private final int successCount;
		private final int failureCount;

		public TriggerResult(int slotCount, int successCount, int failureCount) {
			this.slotCount = slotCount;
			this.successCount = successCount;
			this.failureCount = failureCount;
		}

		public int getSlotCount() { return slotCount; }
		public int getSuccessCount() { return successCount; }
		public int getFailureCount() { return failureCount; }
	}
}
