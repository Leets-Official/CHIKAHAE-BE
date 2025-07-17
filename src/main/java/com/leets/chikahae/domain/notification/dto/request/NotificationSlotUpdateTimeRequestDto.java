
package com.leets.chikahae.domain.notification.dto.request;

import java.time.LocalTime;

public record NotificationSlotUpdateTimeRequestDto(LocalTime sendTime) {
	public static LocalTime toLocalTime(NotificationSlotUpdateTimeRequestDto req) {
		return req.sendTime();
	}
}
