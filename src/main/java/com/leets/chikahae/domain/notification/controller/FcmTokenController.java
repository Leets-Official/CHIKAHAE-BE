package com.leets.chikahae.domain.notification.controller;

import java.util.List;

import com.leets.chikahae.security.auth.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.chikahae.domain.notification.dto.request.FcmTokenRequestDto;
import com.leets.chikahae.domain.notification.dto.response.FcmTokenResponseDto;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FCM Tokens", description = "FCM 토큰 생성·조회·삭제 API")
@RestController
@RequestMapping("/api/users/fcm-tokens")
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    public FcmTokenController(FcmTokenService fcmTokenService) {
        this.fcmTokenService = fcmTokenService;
    }

    /**
     * POST /api/users/{memberId}/fcm-tokens
     * 토큰 등록
     * 요청 예시: { "fcmToken": "FCM_DEVICE_TOKEN" }
     */
    @Operation(
            summary = "FCM 토큰 등록",
            description = "회원의 FCM 토큰을 등록 또는 업데이트합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "등록할 FCM 토큰 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FcmTokenRequestDto.class),
                            examples = @ExampleObject(
                                    name = "등록 예시",
                                    value = "{\"fcmToken\": \"FCM_DEVICE_TOKEN\"}"
                            )
                    )
            )
    )
    @PostMapping
    public ApiResponse<Void> registerToken(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody FcmTokenRequestDto request) {
        fcmTokenService.upsertToken(
                user.getId(),
                FcmTokenRequestDto.toToken(request)
        );
        return ApiResponse.ok(null);
    }

    /**
     * DELETE /api/users/{memberId}/fcm-tokens/{token}
     * 토큰 삭제
     */
    @Operation(
            summary = "FCM 토큰 삭제",
            description = "회원의 특정 FCM 토큰을 삭제합니다."
    )
    @DeleteMapping("/{token}")
    public ApiResponse<Void> deleteToken(
            @AuthenticationPrincipal PrincipalDetails user,
            @PathVariable String token) {
        fcmTokenService.deleteToken(token);
        return ApiResponse.ok(null);
    }

    /**
     * GET /api/users/{memberId}/fcm-tokens
     * 토큰 조회
     */
    @Operation(
            summary = "FCM 토큰 조회",
            description = "회원이 등록한 모든 FCM 토큰을 조회합니다."
    )
    @GetMapping
    public ApiResponse<List<FcmTokenResponseDto>> getTokens(
            @AuthenticationPrincipal PrincipalDetails user
    ) {
        List<FcmTokenResponseDto> response = fcmTokenService.getTokens(user.getId()).stream()
                .map(FcmTokenResponseDto::from)
                .toList();
        return ApiResponse.ok(response);
    }
}
