package com.leets.chikahae.domain.notification.entity;

import java.time.Instant;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "notification_slots",
	uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "slot_type"}))
public class NotificationSlot extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "slot_id" )
	private Long slotId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "slot_type", nullable = false)
	private SlotType slotType;

	@Column(name = "send_time", nullable = false)
	private LocalTime sendTime;

	@Column(name = "next_send_at", nullable = false)
	private Instant nextSendAt;  // Instant 타입 (?)

	@Column(name = "is_enabled", nullable = false)
	private boolean enabled;

	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "message", nullable = false, length = 200)
	private String message;

	protected NotificationSlot() {
	}

	public NotificationSlot(Long memberId, SlotType slotType, LocalTime sendTime,
		Instant nextSendAt, String title, String message) {
		this.memberId = memberId;
		this.slotType = slotType;
		this.sendTime = sendTime;
		this.nextSendAt = nextSendAt;
		this.title = title;
		this.message = message;
	}

}
