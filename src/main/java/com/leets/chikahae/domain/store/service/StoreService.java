package com.leets.chikahae.domain.store.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.point.service.PointService;
import com.leets.chikahae.domain.store.dto.response.PurchaseResponseDto;
import com.leets.chikahae.domain.store.dto.response.ItemResponseDto;
import com.leets.chikahae.domain.store.entity.Item;
import com.leets.chikahae.domain.store.entity.MemberItem;
import com.leets.chikahae.domain.store.repository.ItemRepository;
import com.leets.chikahae.domain.store.repository.MemberItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ItemRepository itemRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberRepository memberRepository;
    private final PointService pointService;

    /**
     * 아이템 전체 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 보유 아이템 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getMyItems(Long memberId) {
        return memberItemRepository.findByMember_MemberId(memberId).stream()
                .map(memberItem -> ItemResponseDto.from(memberItem.getItem()))
                .collect(Collectors.toList());
    }

    /**
     * 아이템 구매 처리
     */
    @Transactional
    public PurchaseResponseDto purchaseItem(Long memberId, Long itemId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        int price = item.getPrice();
        int currentBalance = pointService.getPoint(memberId);

        if (currentBalance < price) {
            return PurchaseResponseDto.builder()
                    .success(false)
                    .message("포인트가 부족합니다.")
                    .remainingCoin(currentBalance)
                    .build();
        }

        // 포인트 차감
        pointService.consumePoint(memberId, price, "아이템 구매 - " + item.getName());

        // 구매 내역 저장
        MemberItem memberItem = MemberItem.builder()
                .member(member)
                .item(item)
                .build();

        memberItemRepository.save(memberItem);

        return PurchaseResponseDto.builder()
                .success(true)
                .message("아이템 구매 성공")
                .remainingCoin(currentBalance - price)
                .build();
    }


}