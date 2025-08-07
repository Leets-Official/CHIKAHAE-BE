package com.leets.chikahae.domain.store.dto.response;

import com.leets.chikahae.domain.store.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponseDto {
    private Long itemId;
    private String name;
    private Integer price;

    public static ItemResponseDto from(Item item) {
        return ItemResponseDto.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }
}
