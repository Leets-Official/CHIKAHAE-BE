package com.leets.chikahae.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
	@NotBlank(message = "닉네임은 필수입니다.")
	String nickname,

	String profileImage
) {}
