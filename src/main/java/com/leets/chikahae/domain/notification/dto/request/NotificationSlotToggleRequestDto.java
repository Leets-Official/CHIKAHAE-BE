package com.leets.chikahae.domain.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 슬롯 활성화/비활성 요청 Dto
@Getter
@NoArgsConstructor
public class NotificationSlotToggleRequestDto {

	private boolean enabled;

	public static boolean toEnabled(NotificationSlotToggleRequestDto req) {
		return req.enabled;
	}
}
