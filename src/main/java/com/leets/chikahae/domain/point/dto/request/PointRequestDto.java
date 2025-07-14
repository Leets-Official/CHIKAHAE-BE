package com.leets.chikahae.domain.point.dto.request;

import lombok.Getter;

@Getter
public class PointRequestDto {
    private Long memberId;
    private int amount;
    private String description;
}
