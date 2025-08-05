package com.leets.chikahae.domain.point.controller;

import com.leets.chikahae.domain.point.dto.request.PointRequestDto;
import com.leets.chikahae.domain.point.dto.response.PointHistoryResponseDto;
import com.leets.chikahae.domain.point.service.PointService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController implements PointControllerSpec {

    private final PointService pointService;

    @GetMapping("/balance")
    public ApiResponse<Integer> getBalance(@AuthenticationPrincipal PrincipalDetails user) {
        int balance = pointService.getPoint(user.getId());
        return ApiResponse.ok(balance);
    }

    @PostMapping("/earn")
    public ApiResponse<Void> earnPoint(@AuthenticationPrincipal PrincipalDetails user,
                                       @RequestBody PointRequestDto request) {
        pointService.earnPoint(user.getId(), request.getAmount(), request.getDescription());
        return ApiResponse.ok(null);
    }

    @PostMapping("/consume")
    public ApiResponse<Void> consumePoint(@AuthenticationPrincipal PrincipalDetails user,
                                          @RequestBody PointRequestDto request) {
        pointService.consumePoint(user.getId(), request.getAmount(), request.getDescription());
        return ApiResponse.ok(null);
    }

    @GetMapping("/history")
    public ApiResponse<List<PointHistoryResponseDto>> getHistory(@AuthenticationPrincipal PrincipalDetails user) {
        try {
            List<PointHistoryResponseDto> history = pointService.getHistory(user.getId());
            return ApiResponse.ok(history);
        } catch (CustomException e) {
            return ApiResponse.fail(e);
        }
    }
}
