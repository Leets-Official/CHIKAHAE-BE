package com.leets.chikahae.domain.member.dto.response;

import java.time.LocalDate;

public record MemberProfileResponse(
	String profileImage,
	String nickname,
	String name,
	boolean gender,
	LocalDate birth
) {}

