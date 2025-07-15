package com.leets.chikahae.domain.point.entity;

import com.leets.chikahae.domain.member.entity.Member;
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

    // --- 비즈니스 메서드 ---

    public void increase(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("포인트는 음수로 증가할 수 없습니다.");
        }
        this.coin += amount;
    }

    public void decrease(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("포인트는 음수로 차감할 수 없습니다.");
        }
        if (this.coin < amount) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
        this.coin -= amount;
    }
}
