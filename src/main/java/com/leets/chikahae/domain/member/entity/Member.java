package com.leets.chikahae.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "kakao_id", nullable = false)
	private String kakaoId;

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;

	@Column(name = "birth", nullable = false)
	private LocalDate birth;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "gender", nullable = false)
	private Boolean gender;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@Column(name = "created_at", nullable = false,  updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public static Member of(
			Long parentId,
			String nickname,
			LocalDate birth,
			Boolean gender,
			String profileImage
	) {
		return Member.builder()
				.parentId(parentId)
				.nickname(nickname)
				.birth(birth)
				.gender(gender)
				.profileImage(profileImage)
				.isDeleted(false)
				.build();
	}


	// ✔️ AuthService에서 사용할 수 있도록 추가
	public Long getId() {
		return this.memberId;
	}

}//class
