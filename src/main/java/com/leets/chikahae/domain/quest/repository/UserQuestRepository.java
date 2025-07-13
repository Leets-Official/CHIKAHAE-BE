package com.leets.chikahae.domain.quest.repository;


import com.leets.chikahae.domain.quest.entity.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {

    // 특정 유저의 오늘 UserQuest 목록
    List<UserQuest> findByMemberIdAndTargetDate(Long memberId, LocalDate targetDate);

    // 특정 유저의 특정 Quest 존재 여부 (중복 방지용)
    boolean existsByMemberIdAndQuest_QuestId(Long memberId, Long questId);
}
