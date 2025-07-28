package com.leets.chikahae.domain.member.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.chikahae.domain.member.dto.request.UpdateProfileRequest;
import com.leets.chikahae.domain.member.dto.response.MemberProfileResponse;
import com.leets.chikahae.domain.member.service.MyPageService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {


	private final MyPageService myPageService;

	@GetMapping("/profile")
	public ApiResponse<MemberProfileResponse> getMyProfile(
		@AuthenticationPrincipal PrincipalDetails principal) {

		Long memberId = principal.getMember().getMemberId();
		MemberProfileResponse response = myPageService.getProfile(memberId);
		return ApiResponse.ok(response);
	}

	@PatchMapping("/profile")
	public ApiResponse<Void> updateMyProfile(
		@AuthenticationPrincipal PrincipalDetails principal,
		@RequestBody @Valid UpdateProfileRequest request) {

		Long memberId = principal.getMember().getMemberId();
		myPageService.updateProfile(memberId, request);
		return ApiResponse.ok(null);
	}
}
