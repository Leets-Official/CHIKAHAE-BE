package com.leets.chikahae.domain.notification.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leets.chikahae.domain.BaseEntity;

import com.leets.chikahae.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notification_slots",
	uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "slot_type"}))
public class NotificationSlot extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "slot_id" )
	private Long slotId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "slot_type", nullable = false)
	private SlotType slotType;

	@Column(name = "send_time", nullable = false)
	private LocalTime sendTime;

	@Column(name = "next_send_at", nullable = false)
	private LocalDateTime nextSendAt;

	@Column(name = "is_enabled", nullable = false)
	private boolean enabled;

	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "message", nullable = false, length = 500)
	private String message;

	protected NotificationSlot() {
	}

	public NotificationSlot(Member member, SlotType slotType,
		LocalTime sendTime, ZoneId zone, String title, String message) {
		this.member   = member;
		this.slotType = slotType;
		this.sendTime  = sendTime;
		this.title    = title;
		this.message  = message;
		this.enabled  = true;
		computeNextSendAt();
	}


	public void changeEnabled(boolean enabled) {
		this.enabled = enabled;
		computeNextSendAt();
	}

	public void changeSendTime(LocalTime sendTime, ZoneId zone) {
		this.sendTime = sendTime;
		computeNextSendAt();

	}

	private void computeNextSendAt() {
		ZoneId zone = ZoneId.systemDefault(); // 수정: zone 인스턴스 필드 대신 로컬로 읽음
		LocalDate tomorrow = LocalDate.now(zone).plusDays(1);
		this.nextSendAt = LocalDateTime.of(tomorrow, this.sendTime);
	}

	public void scheduleNextSend() {
		computeNextSendAt();
	}

}
