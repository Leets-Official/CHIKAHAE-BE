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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "MyPage", description = "마이페이지 프로필 조회/수정 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {


	private final MyPageService myPageService;

	@Operation(
		summary = "프로필 조회",
		description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
	)
	@GetMapping("/profile")
	public ApiResponse<MemberProfileResponse> getMyProfile(
		@AuthenticationPrincipal PrincipalDetails principal) {

		Long memberId = principal.getMember().getMemberId();
		MemberProfileResponse response = myPageService.getProfile(memberId);
		return ApiResponse.ok(response);
	}


	@Operation(
		summary = "내 프로필 수정",
		description = "닉네임 또는 프로필 이미지를 수정합니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "수정할 닉네임 또는 프로필 이미지 정보 (JSON에는 바꿀 필드만 넣으면 됨 아래 예시 참고)",
			content = @Content(
				schema = @Schema(implementation = UpdateProfileRequest.class),
				examples = @ExampleObject(
					name = "요청 예시",
					value = "{\n \"nickname\": \"새로운닉네임\",\n  \"profileImage\": \"https://example.com/image.jpg\"\n}"
				)
			)
		)
	)
	@PatchMapping("/profile")
	public ApiResponse<Void> updateMyProfile(
		@AuthenticationPrincipal PrincipalDetails principal,
		@RequestBody @Valid UpdateProfileRequest request) {

		Long memberId = principal.getMember().getMemberId();
		myPageService.updateProfile(memberId, request);
		return ApiResponse.ok(null);
	}
}
