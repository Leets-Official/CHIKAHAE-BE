package com.leets.chikahae.domain.point.dto.response;

import com.leets.chikahae.domain.point.entity.UserPointHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointHistoryResponseDto {

    @Schema(description = "포인트 증감량 (음수는 소비, 양수는 적립)", example = "50")
    private int amount;

    @Schema(description = "포인트 유형: EARN 또는 CONSUME", example = "EARN")
    private String type;

    @Schema(description = "포인트 내역 설명", example = "퀘스트 보상")
    private String description;

    @Schema(description = "기록 생성 시각", example = "2024-07-07T15:30:00")
    private LocalDateTime createdAt;

    public static PointHistoryResponseDto from(UserPointHistory history) {
        return PointHistoryResponseDto.builder()
                .amount(history.getAmount())
                .type(history.getType().name())
                .description(history.getDescription())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
