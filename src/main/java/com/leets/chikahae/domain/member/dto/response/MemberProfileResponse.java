package com.leets.chikahae.domain.member.dto.response;

import java.time.LocalDate;

public record MemberProfileResponse(
	String nickname,
	String name,
	Boolean gender,
	LocalDate birth
) {}

