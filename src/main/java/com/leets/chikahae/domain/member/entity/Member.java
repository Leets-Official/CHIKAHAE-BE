package com.leets.chikahae.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {
	@Id
	@Column(name = "member_id")
	private Long memberId;

	// 테스트용이므로 나머지 컬럼은 모두 생략해도 OK
	public Member(Long memberId) {
		this.memberId = memberId;
	}
}

