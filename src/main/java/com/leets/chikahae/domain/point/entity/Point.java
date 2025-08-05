package com.leets.chikahae.domain.point.entity;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Point {

    @Id
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // memberId가 FK이자 PK
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int coin;

    public static Point of(Member member) {
        return Point.builder()
                .memberId(member.getId())
                .member(member)
                .coin(0) // 초기 포인트는 0으로 설정
                .build();
    }

    // --- 비즈니스 메서드 ---

    public void increase(int amount) {
        if (amount < 0) {
            throw new CustomException(ErrorCode.NEGATIVE_COIN_AMOUNT, "포인트는 음수로 증가할 수 없습니다.");
        }
        this.coin += amount;
    }

    public void decrease(int amount) {
        if (amount < 0) {
            throw new CustomException(ErrorCode.NEGATIVE_COIN_AMOUNT, "포인트는 음수로 차감할 수 없습니다.");
        }
        if (this.coin < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COIN, "포인트가 부족합니다.");
        }
        this.coin -= amount;
    }

    public void addCoin(int coinReward) {
        if (coinReward < 0) {
            throw new CustomException(ErrorCode.NEGATIVE_COIN_AMOUNT, "포인트는 음수로 추가할 수 없습니다.");
        }
        this.coin += coinReward;
    }
}
