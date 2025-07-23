package com.leets.chikahae.domain.store.controller;

import com.leets.chikahae.security.auth.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.leets.chikahae.domain.store.dto.request.PurchaseRequestDto;
import com.leets.chikahae.domain.store.dto.response.ItemResponseDto;
import com.leets.chikahae.domain.store.dto.response.PurchaseResponseDto;
import com.leets.chikahae.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Store", description = "상점에서 포인트로 아이템 구매하는 API")
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 전체 상점 아이템 목록 조회 API
     * GET /api/store/items
     */
    @Operation(summary = "상점 아이템 전체 조회")
    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        List<ItemResponseDto> items = storeService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * 사용자 보유 아이템 목록 조회 API
     * GET /api/store/items/mine
     */
    @Operation(summary = "보유 아이템 조회")
    @GetMapping("/items/mine")
    public ResponseEntity<List<ItemResponseDto>> getMyItems(@AuthenticationPrincipal PrincipalDetails user) {
        List<ItemResponseDto> myItems = storeService.getMyItems(user.getId());
        return ResponseEntity.ok(myItems);
    }

    /**
     * 아이템 구매 API
     * POST /api/store/purchase
     */
    @Operation(summary = "아이템 구매")
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponseDto> purchaseItem(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody PurchaseRequestDto requestDto
    ) {
        PurchaseResponseDto response = storeService.purchaseItem(user.getId(), requestDto.getItemId());
        return ResponseEntity.ok(response);
    }

}
