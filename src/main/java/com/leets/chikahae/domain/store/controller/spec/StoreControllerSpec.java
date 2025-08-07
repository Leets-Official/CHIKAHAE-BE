package com.leets.chikahae.domain.store.controller.spec;

import com.leets.chikahae.domain.store.dto.request.PurchaseRequestDto;
import com.leets.chikahae.domain.store.dto.response.ItemResponseDto;
import com.leets.chikahae.domain.store.dto.response.PurchaseResponseDto;
import com.leets.chikahae.security.auth.PrincipalDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Store", description = "상점에서 포인트로 아이템 구매하는 API")
@RequestMapping("/api/store")
public interface StoreControllerSpec {

    @Operation(summary = "상점 아이템 전체 조회")
    @GetMapping("/items")
    ResponseEntity<List<ItemResponseDto>> getAllItems();

    @Operation(summary = "보유 아이템 조회")
    @GetMapping("/items/mine")
    ResponseEntity<List<ItemResponseDto>> getMyItems(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user
    );

    @Operation(summary = "아이템 구매")
    @PostMapping("/purchase")
    ResponseEntity<PurchaseResponseDto> purchaseItem(
            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody PurchaseRequestDto requestDto
    );
}
