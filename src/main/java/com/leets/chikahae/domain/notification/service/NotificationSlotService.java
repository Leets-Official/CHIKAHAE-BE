package com.leets.chikahae.domain.notification.service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.domain.notification.repository.NotificationSlotRepository;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;

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
				zone, "아침 알림", "상쾌한 아침을 위한 양치 시간입니다!"),
			new NotificationSlot(member, SlotType.LUNCH,   LocalTime.of(12, 30),
				zone, "점심 알림", "점심 식사 후 상쾌함을 위해 양치하세요!"),
			new NotificationSlot(member, SlotType.EVENING, LocalTime.of(21, 0),
				zone, "저녁 알림", "하루 마무리 전, 잊지 말고 양치합시다!" )
		);
		notificationSlotRepo.saveAll(slots);
	}


	//활성화 된 슬롯 조회
	public List<NotificationSlot> findDueSlot(Instant nowUtc) {
		return notificationSlotRepo.findByEnabledTrueAndNextSendAtBefore(nowUtc);
	}

	//슬롯 on/off - Long memberId로 수정
	@Transactional
	public void toggleSlot(Long memberId, SlotType type, boolean enabled) {
		NotificationSlot slot = notificationSlotRepo
			.findByMember_MemberIdAndSlotType(memberId, type)
			.orElseThrow(() -> new CustomException(
				ErrorCode.SLOT_NOT_FOUND,
				"memberId=" + memberId + ", slotType=" + type
			));
		slot.changeEnabled(enabled);
	}

	//슬롯 시간 변경 - Long memberId로 수정
	@Transactional
	public void updateSlotTime(Long memberId, SlotType type,
		LocalTime sendTime, ZoneId zone) {
		NotificationSlot slot = notificationSlotRepo
			.findByMember_MemberIdAndSlotType(memberId, type)
			.orElseThrow(() -> new CustomException(
				ErrorCode.SLOT_NOT_FOUND,
				"memberId=" + memberId + ", slotType=" + type
			));
		slot.changeSendTime(sendTime, zone);
	}

	// Member ID로 조회하도록 수정
	public List<NotificationSlot> getSlots(Long memberId) {
		return notificationSlotRepo.findByMember_MemberId(memberId);
	}
}
