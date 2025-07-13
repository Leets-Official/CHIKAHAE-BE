package com.leets.chikahae.domain.quest.dto;

import com.leets.chikahae.domain.quest.entity.UserQuest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuestResponseDto {

    private Long userQuestId;
    private int step;
    private int point;
    private String status;
    private LocalDateTime rewardAt;

    public static QuestResponseDto from(UserQuest userQuest) {
        return QuestResponseDto.builder()
                .userQuestId(userQuest.getUserQuestId())
                .step(userQuest.getQuest().getStep())
                .point(userQuest.getQuest().getPoint())
                .status(userQuest.getStatus().name())
                .rewardAt(userQuest.getRewardAt())
                .build();
    }
}
