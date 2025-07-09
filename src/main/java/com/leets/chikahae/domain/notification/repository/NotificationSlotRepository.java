package com.leets.chikahae.domain.notification.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.entity.SlotType;

@Repository
public interface NotificationSlotRepository extends JpaRepository<NotificationSlot, Long> {

	/**
	 * 활성화된 슬롯 중, nextSendAt이 현재 시각 이전(또는 같은)인 슬롯을 조회
	 * 스케줄러에서 전송 대상 슬롯을 찾을 때 사용
	 */

	List<NotificationSlot> findByEnabledTrueAndNextSendAtBefore(Instant now);

	/**
	 * 특정 회원(Member)과 슬롯 타입(SlotType)에 해당하는 슬롯을 조회
	 * ON/OFF 토글 및 시간 변경 시 해당 슬롯을 가져올 때 사용
	 */
	Optional<NotificationSlot> findByMemberAndSlotType(Member meber, SlotType slotType);

	/**
	 * 특정 회원의 모든 슬롯을 조회
	 * 마이페이지에서 사용자 설정 전체를 보여줄 때 사용
	 */
	List<NotificationSlot> findByMember(Member member);
}
