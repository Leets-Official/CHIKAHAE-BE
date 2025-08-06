package com.leets.chikahae.domain.mission.controller;

import com.leets.chikahae.domain.mission.controller.spec.MissionControllerSpec;
import com.leets.chikahae.domain.mission.dto.MissionResponse;
import com.leets.chikahae.domain.mission.entity.Mission;
import com.leets.chikahae.domain.mission.service.MissionService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController implements MissionControllerSpec {

    private final MissionService missionService;

    @Override
    public ApiResponse<List<MissionResponse>> getTodayMissions(PrincipalDetails user) {
        List<MissionResponse> responses = missionService.getAllMissions(user.getId());
        return ApiResponse.ok(responses);
    }

    @Override
    public ApiResponse<Integer> completeMission(PrincipalDetails user, String missionCode) {
        int point = missionService.completeMission(user.getMember(), Mission.MissionCode.valueOf(missionCode));
        return ApiResponse.ok(point);
    }
}
