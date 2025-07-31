package com.leets.chikahae.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
	String nickname,
	String profileImage
) {}
