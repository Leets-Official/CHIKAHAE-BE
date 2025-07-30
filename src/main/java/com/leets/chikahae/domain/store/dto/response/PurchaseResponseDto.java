package com.leets.chikahae.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseResponseDto {
    private boolean success;
    private String message;
    private int remainingCoin;
}
