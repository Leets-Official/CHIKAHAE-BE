package com.leets.chikahae.domain.point.controller;

import com.leets.chikahae.domain.point.dto.request.PointRequestDto;
import com.leets.chikahae.domain.point.dto.response.PointHistoryResponseDto;
import com.leets.chikahae.security.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Point", description = "포인트(치카코인) 관련 API")
public interface PointControllerSpec {

    @Operation(summary = "현재 잔액 조회", description = "memberId에 해당하는 사용자의 현재 치카코인 잔액을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 현재 잔액 반환")
    ResponseEntity<Integer> getBalance(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user
    );

    @Operation(summary = "포인트 적립", description = "요청한 amount만큼 포인트를 적립하고 기록합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 적립 성공")
    ResponseEntity<Void> earnPoint(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody PointRequestDto request
    );

    @Operation(summary = "포인트 소비", description = "요청한 amount만큼 포인트를 소비하고 기록합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 소비 성공")
    ResponseEntity<Void> consumePoint(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody PointRequestDto request
    );
    @Operation(summary = "포인트 이력 조회", description = "memberId에 해당하는 사용자의 포인트 증감 이력을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 이력 반환 성공")
    ResponseEntity<List<PointHistoryResponseDto>> getHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user
    );}
