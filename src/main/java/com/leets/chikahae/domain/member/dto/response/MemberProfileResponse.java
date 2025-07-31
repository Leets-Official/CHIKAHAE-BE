package com.leets.chikahae.domain.member.dto.response;

import com.leets.chikahae.domain.member.entity.Gender;

import java.time.LocalDate;

public record MemberProfileResponse(
	String profileImage,
	String nickname,
	String kakaoEmail,
	Gender gender,
	LocalDate birth
) {}

