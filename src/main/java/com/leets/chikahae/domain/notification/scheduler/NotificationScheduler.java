package com.leets.chikahae.domain.notification.scheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.repository.NotificationSlotRepository;
import com.leets.chikahae.domain.notification.service.FcmPushService;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationScheduler {

	private final FcmPushService fcmPushService;
	private final FcmTokenService fcmTokenService;
	private final NotificationSlotRepository notificationSlotRepository;
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");


	public NotificationScheduler( FcmPushService fcmPushService,
		NotificationSlotRepository notificationSlotRepository ,FcmTokenService fcmTokenService) {
		this.fcmPushService = fcmPushService;
		this.notificationSlotRepository = notificationSlotRepository;
		this.fcmTokenService = fcmTokenService;
	}

	@Scheduled(cron = "0 0/1 * * * *", zone= "Asia/Seoul")  // 매 분 0초에 실행
	@Transactional
	public void runScheduler() {
		// 1) 현재 KST 시:분
		LocalTime nowTime = LocalDateTime
			.now(KST)
			.truncatedTo(ChronoUnit.MINUTES)
			.toLocalTime();

		log.info("▶ runScheduler — nowTime={}", nowTime);

		// 2) sendTime == nowTime && enabled인 슬롯만 조회
		List<NotificationSlot> dueSlots =
			notificationSlotRepository.findBySendTimeAndEnabled(nowTime, true);
		log.info(" dueSlots 조회 — sendTime={}인 슬롯 개수={}", nowTime, dueSlots.size());

		// 3) 푸시 전송 & 다음 예약 재계산
		for (NotificationSlot slot : dueSlots) {
			List<String> tokens = fcmTokenService
				.getTokens(slot.getMember().getMemberId())
				.stream()
				.map(t -> t.getFcmToken())
				.toList();

			if (!tokens.isEmpty()) {
				fcmPushService.sendToEach(tokens, slot.getTitle(), slot.getMessage());
			}

			// sendTime 기반으로 내일 같은 시각으로 nextSendAt 재설정
			slot.scheduleNextSend();
		}
	}
}

