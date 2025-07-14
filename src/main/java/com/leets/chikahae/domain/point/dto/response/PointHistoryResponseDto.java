package com.leets.chikahae.domain.point.dto.response;

import com.leets.chikahae.domain.point.entity.UserPointHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointHistoryResponseDto {
    private int amount;
    private String type;
    private String description;
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
