package com.leets.chikahae.domain.quest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_quest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userQuestId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate targetDate; // 할당된 날짜

    private LocalDateTime rewardAt; // 포인트 수령 시간

    public enum Status {
        WAITING, COMPLETED, REWARDED
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
    }

    public void markRewarded() {
        this.status = Status.REWARDED;
        this.rewardAt = LocalDateTime.now();
    }
}
