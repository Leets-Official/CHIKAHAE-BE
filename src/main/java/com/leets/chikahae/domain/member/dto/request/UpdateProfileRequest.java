package com.leets.chikahae.domain.member.dto.request;

public record UpdateProfileRequest(
	String nickname,
	String profileImage
) {}
