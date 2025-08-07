package com.leets.chikahae.domain.notification.controller.spec;

import com.leets.chikahae.domain.notification.dto.request.FcmTokenRequestDto;
import com.leets.chikahae.domain.notification.dto.response.FcmTokenResponseDto;
import com.leets.chikahae.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.leets.chikahae.security.auth.PrincipalDetails;

import java.util.List;

@Tag(name = "FCM Tokens", description = "FCM 토큰 생성·조회·삭제 API")
@RequestMapping("/api/users/fcm-tokens")
public interface FcmTokenControllerSpec {

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
    ApiResponse<Void> registerToken(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody FcmTokenRequestDto request
    );

    @Operation(
            summary = "FCM 토큰 삭제",
            description = "회원의 특정 FCM 토큰을 삭제합니다."
    )
    @DeleteMapping("/{token}")
    ApiResponse<Void> deleteToken(
            @AuthenticationPrincipal PrincipalDetails user,
            @PathVariable String token
    );

    @Operation(
            summary = "FCM 토큰 조회",
            description = "회원이 등록한 모든 FCM 토큰을 조회합니다."
    )
    @GetMapping
    ApiResponse<List<FcmTokenResponseDto>> getTokens(
            @AuthenticationPrincipal PrincipalDetails user
    );

}
