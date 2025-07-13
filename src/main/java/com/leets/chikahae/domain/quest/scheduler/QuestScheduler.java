package com.leets.chikahae.domain.quest.scheduler;

import com.leets.chikahae.domain.quest.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestScheduler {

    private final QuestService questService;

    /**
     * 매일 자정(00:00)에 오늘의 Quest를 생성하고 모든 유저에게 할당
     */
    @Scheduled(cron = "0 0 0 * * *")  // 매일 00:00에 실행
    public void runDailyQuestGeneration() {
        log.info("[QuestScheduler] 일일 Quest 생성 및 할당 스케줄러 시작");

        try {
            questService.generateDailyQuests();  // 비즈니스 로직은 Service에 위임
            log.info("[QuestScheduler] 일일 Quest 생성 및 할당 완료");
        } catch (Exception e) {
            log.error("[QuestScheduler] 일일 Quest 생성 중 에러 발생: {}", e.getMessage(), e);
        }
    }
}
