package com.leets.chikahae.domain.notification.dto.request;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

//슬롯 발송 시간 변경 dto
@Getter
@NoArgsConstructor
public class NotificationSlotUpdateTimeRequestDto {

	private LocalTime sendTime;

	public static LocalTime toLocalTime(NotificationSlotUpdateTimeRequestDto req) {
		return req.sendTime;
	}
}
