
package com.leets.chikahae.domain.notification.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record NotificationSlotUpdateTimeRequestDto(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")LocalTime sendTime) {
	public static LocalTime toLocalTime(NotificationSlotUpdateTimeRequestDto req) {
		return req.sendTime();
	}
}
