package com.leets.chikahae.domain.member.controller.spec;

import com.leets.chikahae.domain.member.dto.request.UpdateProfileRequest;
import com.leets.chikahae.domain.member.dto.response.MemberProfileResponse;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.leets.chikahae.security.auth.PrincipalDetails;

@Tag(name = "MyPage", description = "마이페이지 프로필 조회/수정 API")
@RequestMapping("/api/mypage")
public interface MyPageControllerSpec {

    @Operation(
            summary = "프로필 조회",
            description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
    )
    @GetMapping("/profile")
    ApiResponse<MemberProfileResponse> getMyProfile(
            @AuthenticationPrincipal PrincipalDetails principal
    );

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
    ApiResponse<Void> updateMyProfile(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody @Valid UpdateProfileRequest request
    );

}
