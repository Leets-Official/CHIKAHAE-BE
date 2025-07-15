package com.leets.chikahae.domain.notification.dto.request;

public record NotificationSlotToggleRequestDto(boolean enabled) {
	public static boolean toEnabled(NotificationSlotToggleRequestDto req) {
		return req.enabled();
	}
}
