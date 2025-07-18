package com.leets.chikahae.domain.point.controller;

import com.leets.chikahae.domain.point.dto.request.PointRequestDto;
import com.leets.chikahae.domain.point.dto.response.PointHistoryResponseDto;
import com.leets.chikahae.domain.point.service.PointService;
import com.leets.chikahae.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController implements PointControllerSpec{

    private final PointService pointService;

    /**
     * 현재 잔액 반환 API
     * GET /api/points/balance?memberId=X
     */
    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance(@AuthenticationPrincipal PrincipalDetails user) {
        int balance = pointService.getPoint(user.getId());
        return ResponseEntity.ok(balance);
    }

    /**
     * 포인트 적립 API
     * POST /api/points/earn
     */
    @PostMapping("/earn")
    public ResponseEntity<Void> earnPoint(@AuthenticationPrincipal PrincipalDetails user,@RequestBody PointRequestDto request) {
        pointService.earnPoint(user.getId(), request.getAmount(), request.getDescription());
        return ResponseEntity.ok().build();
    }

    /**
     * 포인트 소비 API
     * POST /api/points/consume
     */
    @PostMapping("/consume")
    public ResponseEntity<Void> consumePoint(@AuthenticationPrincipal PrincipalDetails user,@RequestBody PointRequestDto request) {
        pointService.consumePoint(user.getId(), request.getAmount(), request.getDescription());
        return ResponseEntity.ok().build();
    }

    /**
     * 포인트 이력 조회 API
     * GET /api/points/history?memberId=X
     */
    @GetMapping("/history")
    public ResponseEntity<List<PointHistoryResponseDto>> getHistory(@AuthenticationPrincipal PrincipalDetails user) {
        List<PointHistoryResponseDto> history = pointService.getHistory(user.getId());
        return ResponseEntity.ok(history);
    }
}
