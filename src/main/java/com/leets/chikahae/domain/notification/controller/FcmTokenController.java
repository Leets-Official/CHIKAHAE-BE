package com.leets.chikahae.domain.notification.controller;

import com.leets.chikahae.domain.notification.controller.spec.FcmTokenControllerSpec;
import com.leets.chikahae.domain.notification.dto.request.FcmTokenRequestDto;
import com.leets.chikahae.domain.notification.dto.response.FcmTokenResponseDto;
import com.leets.chikahae.domain.notification.service.FcmTokenService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FcmTokenController implements FcmTokenControllerSpec {

    private final FcmTokenService fcmTokenService;

    @Override
    public ApiResponse<Void> registerToken(PrincipalDetails user, FcmTokenRequestDto request) {
        fcmTokenService.upsertToken(
                user.getId(),
                FcmTokenRequestDto.toToken(request)
        );
        return ApiResponse.ok(null);
    }

    @Override
    public ApiResponse<Void> deleteToken(PrincipalDetails user, String token) {
        fcmTokenService.deleteToken(token);
        return ApiResponse.ok(null);
    }

    @Override
    public ApiResponse<List<FcmTokenResponseDto>> getTokens(PrincipalDetails user) {
        List<FcmTokenResponseDto> response = fcmTokenService.getTokens(user.getId()).stream()
                .map(FcmTokenResponseDto::from)
                .toList();
        return ApiResponse.ok(response);
    }
}
