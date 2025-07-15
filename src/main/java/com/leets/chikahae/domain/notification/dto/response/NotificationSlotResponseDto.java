package com.leets.chikahae.domain.notification.dto.response;

import java.time.LocalTime;
import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.entity.SlotType;

public record NotificationSlotResponseDto(
	SlotType slotType,
	LocalTime sendTime,
	boolean enabled,
	String title,
	String message
) {
	public static NotificationSlotResponseDto from(NotificationSlot slot) {
		return new NotificationSlotResponseDto(
			slot.getSlotType(),
			slot.getSendTime(),
			slot.isEnabled(),
			slot.getTitle(),
			slot.getMessage()
		);
	}
}
