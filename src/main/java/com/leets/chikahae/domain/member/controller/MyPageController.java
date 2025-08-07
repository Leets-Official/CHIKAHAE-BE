package com.leets.chikahae.domain.member.controller;

import com.leets.chikahae.domain.member.controller.spec.MyPageControllerSpec;
import com.leets.chikahae.domain.member.dto.request.UpdateProfileRequest;
import com.leets.chikahae.domain.member.dto.response.MemberProfileResponse;
import com.leets.chikahae.domain.member.service.MyPageService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController implements MyPageControllerSpec {

	private final MyPageService myPageService;

	@Override
	public ApiResponse<MemberProfileResponse> getMyProfile(PrincipalDetails principal) {
		Long memberId = principal.getMember().getMemberId();
		MemberProfileResponse response = myPageService.getProfile(memberId);
		return ApiResponse.ok(response);
	}

	@Override
	public ApiResponse<Void> updateMyProfile(PrincipalDetails principal, UpdateProfileRequest request) {
		Long memberId = principal.getMember().getMemberId();
		myPageService.updateProfile(memberId, request);
		return ApiResponse.ok(null);
	}
}
