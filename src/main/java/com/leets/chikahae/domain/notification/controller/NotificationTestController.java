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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	) {
		// 1) 활성화된 모든 슬롯을 가져온다
		List<NotificationSlot> allSlots = slotService.getSlots(user.getId())
			.stream()
			.filter(NotificationSlot::isEnabled)
			.toList();

			log.info("▶ Trigger FCM push for {} slots", allSlots.size());

		// 2) 슬롯별로 토큰 조회 → sendToEach 호출 (서비스에서 개별 로그)
		for (NotificationSlot slot : allSlots) {
			Long slotId = slot.getSlotId();       // slot.getId() 가 없으면 실제 필드명 확인
			String title = slot.getTitle();
			String message = slot.getMessage();

			log.info("→ Processing slotId={} | title='{}' | message='{}'", slotId, title, message);

			List<String> tokens = tokenService.getTokens(slot.getMember().getMemberId())
				.stream()
				.map(FcmToken::getFcmToken)
				.collect(Collectors.toList());

			if (tokens.isEmpty()) {
				log.warn("⚠ No tokens found for slotId={}", slotId);
				continue;
			}

			pushService.sendToEach(tokens, title, message);
		}

		// 3) 처리된 슬롯 개수 반환
		return ApiResponse.ok(new TriggerResult(allSlots.size()));
	}

	public static class TriggerResult {
		private final int slotCount;

		public TriggerResult(int slotCount) {
			this.slotCount = slotCount;
		}

		public int getSlotCount() {
			return slotCount;
		}
	}
}
