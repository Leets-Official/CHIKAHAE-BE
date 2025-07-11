package com.leets.chikahae.domain.notification.dto.response;

import java.time.LocalTime;

import com.leets.chikahae.domain.notification.entity.NotificationSlot;
import com.leets.chikahae.domain.notification.entity.SlotType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSlotResponseDto {

	private final SlotType slotType;
	private final LocalTime sendTime;
	private final boolean enabled;
	private final String title;
	private final String message;

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
