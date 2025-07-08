package com.leets.chikahae.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class FcmToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fcm_token_id")
	private Long tokenId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "fcm_token", nullable = false)
	private String fcmToken;

	protected FcmToken() {
	}

	public FcmToken(Long memberId, String fcmToken) {
		this.memberId = memberId;
		this.fcmToken = fcmToken;
	}
}
