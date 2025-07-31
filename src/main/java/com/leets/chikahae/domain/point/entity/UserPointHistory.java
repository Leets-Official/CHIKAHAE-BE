package com.leets.chikahae.domain.point.entity;

import com.leets.chikahae.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_point_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserPointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private int amount; // 양수 = EARN, 음수 = CONSUME

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(length = 255)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate date;

    // --- enum 정의 ---
    public enum Type {
        EARN, CONSUME
    }

    @PrePersist
    public void autoSetDate() {
        this.date = LocalDate.now();
    }
}
