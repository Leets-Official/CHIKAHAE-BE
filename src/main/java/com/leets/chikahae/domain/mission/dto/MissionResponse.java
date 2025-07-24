package com.leets.chikahae.domain.mission.dto;

import com.leets.chikahae.domain.mission.entity.Mission;
import lombok.Builder;

@Builder
public record MissionResponse(
    Long missionId,
    String title,
    String description,
    String code,
    Integer point,
    Boolean isCompleted
) {

    public static MissionResponse from(Mission mission,Boolean isCompleted) {
        return MissionResponse.builder()
                .missionId(mission.getMissionId())
                .title(mission.getName())
                .description(mission.getDescription())
                .code(mission.getCode().name())
                .point(mission.getRewardPoint())
                .isCompleted(isCompleted)
                .build();
    }
}
