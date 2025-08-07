package com.leets.chikahae.domain.notification.controller.spec;

import com.leets.chikahae.domain.notification.dto.request.NotificationSlotToggleRequestDto;
import com.leets.chikahae.domain.notification.dto.request.NotificationSlotUpdateTimeRequestDto;
import com.leets.chikahae.domain.notification.dto.response.NotificationSlotResponseDto;
import com.leets.chikahae.domain.notification.entity.SlotType;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification Slots", description = "알림 슬롯 조회·수정 API")
@RequestMapping("/api/notifications/slots")
public interface NotificationSlotControllerSpec {

    @Operation(
            summary = "알림 슬롯 조회",
            description = "현재 사용자의 모든 알림 슬롯 설정을 조회합니다."
    )
    @GetMapping
    ApiResponse<List<NotificationSlotResponseDto>> getSlots(
            @AuthenticationPrincipal PrincipalDetails user
    );

    @Operation(
            summary = "슬롯 시간대 변경",
            description = "지정한 슬롯의 발송 시간을 업데이트합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "변경할 발송 시간 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationSlotUpdateTimeRequestDto.class),
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    value = "{\"sendTime\": \"HH:mm\"}"
                            )
                    )
            )
    )
    @PatchMapping("/{slotType}/time")
    ApiResponse<Void> updateTime(
            @AuthenticationPrincipal PrincipalDetails user,
            @PathVariable SlotType slotType,
            @RequestBody NotificationSlotUpdateTimeRequestDto request
    );

    @Operation(
            summary = "슬롯 on/off 토글",
            description = "지정한 슬롯의 활성화 상태를 변경합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "토글할 활성화 상태 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationSlotToggleRequestDto.class),
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    value = "{\"enabled\": true}"
                            )
                    )
            )
    )
    @PatchMapping("/{slotType}/enabled")
    ApiResponse<Void> toggleSlot(
            @AuthenticationPrincipal PrincipalDetails user,
            @PathVariable SlotType slotType,
            @RequestBody NotificationSlotToggleRequestDto request
    );

    @Operation(
            summary = "전체 슬롯 on/off 토글",
            description = "현재 사용자의 모든 알림 슬롯 활성화 상태를 변경합니다."
    )
    @PatchMapping("/enabled")
    ApiResponse<Void> toggleAllSlots(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody NotificationSlotToggleRequestDto request
    );
}
