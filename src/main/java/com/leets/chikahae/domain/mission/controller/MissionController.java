package com.leets.chikahae.domain.mission.controller;

import com.leets.chikahae.domain.mission.dto.MissionResponse;
import com.leets.chikahae.domain.mission.service.MissionService;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mission", description = "미션 조회, 상태 변경 API")
@RestController
@RequestMapping("/api/mission")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 오늘의 미션 목록 조회
    @GetMapping("/today")
    public ApiResponse<List<MissionResponse>> getTodayMissions(@AuthenticationPrincipal PrincipalDetails user) {
        List<MissionResponse> responses = missionService.getAllMissions(user.getId());
        return ApiResponse.ok(responses);
    }


}
