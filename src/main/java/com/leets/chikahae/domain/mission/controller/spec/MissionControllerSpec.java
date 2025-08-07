package com.leets.chikahae.domain.mission.controller.spec;

import com.leets.chikahae.domain.mission.dto.MissionResponse;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mission", description = "미션(양치 미션 등) 관련 API")
@RequestMapping("/api/missions")
public interface MissionControllerSpec {

    @Operation(
            summary = "오늘의 미션 조회",
            description = "현재 로그인한 사용자의 오늘 미션 리스트를 반환합니다."
    )
    @GetMapping("/today")
    ApiResponse<List<MissionResponse>> getTodayMissions(
            @AuthenticationPrincipal PrincipalDetails user
    );

    @Operation(
            summary = "미션 완료 처리",
            description = "전달된 미션 코드에 해당하는 미션을 완료하고 보상 포인트를 반환합니다."
    )
    @PostMapping("/complete")
    ApiResponse<Integer> completeMission(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody String missionCode
    );
}
