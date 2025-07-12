package com.leets.chikahae.domain.notification.scheduler;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.service.FcmPushService;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;

@Component
public class NotificationScheduler {

	private final NotificationSlotService notificationSlotService;
	private final FcmPushService fcmPushService;
	private final FcmTokenService fcmTokenService;
	private final ZoneId zone = ZoneId.systemDefault();

	public NotificationScheduler(NotificationSlotService notificationSlotService, FcmPushService fcmPushService,
		FcmTokenService fcmTokenService) {
		this.notificationSlotService = notificationSlotService;
		this.fcmPushService = fcmPushService;
		this.fcmTokenService = fcmTokenService;
	}

	@Scheduled(cron = "0 0/1 * * * *") // 매 분마다 실행 예시
	@Transactional
	public void runScheduler() throws Exception {
		Instant now = Instant.now();
		// 인스턴스 메서드 호출로 수정: findDueSlots
		List<NotificationSlot> dueSlots = notificationSlotService.findDueSlot(now);

		for (NotificationSlot slot : dueSlots) {
			// 토큰 조회: 인스턴스 메서드 호출 (멤버아이디까지 가져오게끔 수정 07/12)
			List<String> tokens = fcmTokenService.getTokens(slot.getMember().getMemberId()).stream()
				.map(t -> t.getFcmToken())
				.collect(Collectors.toList());

			if (!tokens.isEmpty()) {
				// 푸시 전송: 인스턴스 메서드 호출
				fcmPushService.sendMulticast(tokens, slot.getTitle(), slot.getMessage());
			}
			// 다음 전송 시각 재계산: 오늘 sendTime 기준 +1일
			slot.changeSendTime(slot.getSendTime(), zone);
		}
	}
}

