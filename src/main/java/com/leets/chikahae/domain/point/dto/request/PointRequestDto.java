package com.leets.chikahae.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PointRequestDto {

    @Min(1)
    @Schema(description = "포인트 증감량", example = "50 (1 이상이어야 함)")
    private int amount;

    @NotBlank
    @Schema(description = "포인트 사용/적립에 대한 설명", example = "퀘스트 리워드")
    private String description;
}
