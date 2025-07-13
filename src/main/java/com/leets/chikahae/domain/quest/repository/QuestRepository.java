package com.leets.chikahae.domain.quest.repository;

import com.leets.chikahae.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {

    // 오늘 Quest step=1 데이터 존재 여부 체크 (중복 방지용)
    boolean existsByStepAndCreatedAtBetween(int step, LocalDateTime start, LocalDateTime end);

    // 오늘 생성된 Quest 전체 조회
    @Query("SELECT q FROM Quest q WHERE q.createdAt >= :start AND q.createdAt < :end ORDER BY q.step ASC")
    List<Quest> findQuestsByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
