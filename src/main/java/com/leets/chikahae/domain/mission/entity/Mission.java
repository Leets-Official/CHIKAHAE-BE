package com.leets.chikahae.domain.mission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionCode code; // 예: "DAILY_QUIZ", "FIRST_LOGIN"

    @Column(nullable = false)
    private String name; // 화면에 보여줄 미션명

    private String description;

    @Column(nullable = false)
    private int rewardPoint;  // 미션 유형에 따른 고정 보상 포인트


    public enum MissionCode {
        DAILY_QUIZ(1),
        ANIMATION(3);

        private final int rewardPoint;

        MissionCode(int rewardPoint) {
            this.rewardPoint = rewardPoint;
        }

        public int getRewardPoint() {
            return rewardPoint;
        }
    }

}
