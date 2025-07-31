package com.leets.chikahae.domain.store.init;

import com.leets.chikahae.domain.store.entity.Item;
import com.leets.chikahae.domain.store.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        if (itemRepository.count() == 0) {
            itemRepository.save(Item.builder().name("브론즈 칫솔").price(50).image("bronze_toothbrush.png").build());
            itemRepository.save(Item.builder().name("실버 칫솔").price(100).image("silver_toothbrush.png").build());
            itemRepository.save(Item.builder().name("금 칫솔").price(150).image("gold_toothbrush.png").build());
        }
    }
}
