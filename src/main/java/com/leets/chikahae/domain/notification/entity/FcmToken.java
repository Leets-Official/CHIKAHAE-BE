package com.leets.chikahae.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FcmToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fcm_token_id")
	private Long tokenId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "fcm_token", nullable = false)
	private String fcmToken;

	protected FcmToken() {
	}

	public FcmToken(Long memberId, String fcmToken) {
		this.memberId = memberId;
		this.fcmToken = fcmToken;
	}
}
