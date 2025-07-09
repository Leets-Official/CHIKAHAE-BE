package com.leets.chikahae.domain.notification.service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.domain.notification.repository.NotificationSlotRepository;

@Service
public class NotificationSlotService {

	private final NotificationSlotRepository notificationSlotRepo;

	public NotificationSlotService(NotificationSlotRepository notificationSlotRepo) {
		this.notificationSlotRepo = notificationSlotRepo;
	}

	// 사용자별로, 기본 3개 슬롯 생성
	@Transactional
	public void createDefaultSlots(Member member, ZoneId zone) {
		Instant nowUtc = Instant.now();
		List<NotificationSlot> slots = List.of(
			new NotificationSlot(member, SlotType.MORNING, LocalTime.of(7, 30),
				nowUtc, "아침 알림", "상쾌한 아침을 위한 양치 시간입니다!"),
			new NotificationSlot(member, SlotType.LUNCH,   LocalTime.of(12, 30),
				nowUtc, "점심 알림", "점심 식사 후 상쾌함을 위해 양치하세요!"),
			new NotificationSlot(member, SlotType.EVENING, LocalTime.of(21, 0),
				nowUtc, "저녁 알림", "하루 마무리 전, 잊지 말고 양치합시다!" )
		);
		notificationSlotRepo.saveAll(slots);
	}


	//활성화 된 슬롯 조회
	public List<NotificationSlot> findDueSlot(Instant nowUtc) {
		return notificationSlotRepo.findByEnabledTrueAndNextSendAtBefore(nowUtc);
	}

	//슬롯 on/off (enable -> false or true로)
	@Transactional
	public void toggleSlot(Member member, SlotType type, boolean enabled) {
		NotificationSlot slot = notificationSlotRepo
			.findByMemberAndSlotType(member, type)
			.orElseThrow(() -> new IllegalArgumentException("슬롯이 없음")); // 공통예외로 빼야함
		slot.changeEnabled(enabled);
	}


	//슬롯 시간 변경 + nextSendAt 재계산
	@Transactional
	public void updateSlotTime(Member member, SlotType type, LocalTime sendTime, ZoneId zone) {
		NotificationSlot slot = notificationSlotRepo
			.findByMemberAndSlotType(member, type)
			.orElseThrow(() -> new IllegalArgumentException("슬롯이 없음")); //공통예외로 뺴야함
		slot.changeSendTime(sendTime, zone);
	}


	//해당 회원의 모든 슬롯 조회
	public List<NotificationSlot> getSlots(Member member) {
		return notificationSlotRepo.findByMember(member);
	}

}
