package com.leets.chikahae.domain.quest.controller;

import com.leets.chikahae.domain.quest.dto.QuestResponseDto;
import com.leets.chikahae.domain.quest.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    /**
     * 🔹 오늘의 퀘스트 목록 조회 API
     * GET /api/quests/today?memberId=1
     */
    @GetMapping("/today")
    public ResponseEntity<List<QuestResponseDto>> getTodayQuests(@RequestParam Long memberId) {
        List<QuestResponseDto> quests = questService.getTodayQuestsForMember(memberId);
        return ResponseEntity.ok(quests);
    }

    /**
     * 🔹 퀘스트 완료 상태 확인 API
     * POST /api/quests/{userQuestId}/check-completion
     */
    @PostMapping("/{userQuestId}/check-completion")
    public ResponseEntity<Void> checkQuestCompletion(@PathVariable Long userQuestId) {
        questService.checkAndMarkCompleted(userQuestId);
        return ResponseEntity.ok().build();
    }

    /**
     * 🔹 리워드 지급 API
     * POST /api/quests/{userQuestId}/reward
     */
    @PostMapping("/{userQuestId}/reward")
    public ResponseEntity<Void> rewardQuest(@PathVariable Long userQuestId) {
        questService.reward(userQuestId);
        return ResponseEntity.ok().build();
    }
}
