package com.leets.chikahae.domain.quest.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.quest.dto.QuestResponseDto;
import com.leets.chikahae.domain.quest.entity.Quest;
import com.leets.chikahae.domain.quest.entity.UserQuest;
import com.leets.chikahae.domain.quest.repository.QuestRepository;
import com.leets.chikahae.domain.quest.repository.UserQuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final UserQuestRepository userQuestRepository;
    private final MemberRepository memberRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;

    /**
     * 오늘의 퀘스트 목록 조회
     */
    @Transactional(readOnly = true)
    public List<QuestResponseDto> getTodayQuestsForMember(Long memberId) {
        LocalDate today = LocalDate.now();
        List<UserQuest> userQuests = userQuestRepository.findByMemberIdAndTargetDate(memberId, today);
        return userQuests.stream()
                .map(QuestResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 퀘스트 완료 조건 확인 및 상태 변경
     * (애니메이션+퀴즈 완료 여부는 다른 도메인 서비스에서 가져와야 함 → 여기선 stub로 예시)
     */
    @Transactional
    public void checkAndMarkCompleted(Long userQuestId) {
        UserQuest userQuest = userQuestRepository.findById(userQuestId)
                .orElseThrow(() -> new IllegalArgumentException("UserQuest not found"));

        if (userQuest.getStatus() != UserQuest.Status.WAITING) {
            log.info("[QuestService] 이미 상태 변경된 퀘스트: {}", userQuestId);
            return;
        }

        // 🔔 실제로는 animationService / quizService로 완료 여부 체크해야 함
        boolean animationDone = true; // stub: 조건 만족했다고 가정
        boolean quizDone = true;

        if (animationDone && quizDone) {
            userQuest.markCompleted();
            log.info("[QuestService] UserQuest {} COMPLETED 처리 완료", userQuestId);
        }
    }

    /**
     * 리워드 지급: 포인트 적립 + 상태 변경 + 이력 저장
     */
    @Transactional
    public void reward(Long userQuestId) {
        UserQuest userQuest = userQuestRepository.findById(userQuestId)
                .orElseThrow(() -> new IllegalArgumentException("UserQuest not found"));

        if (userQuest.getStatus() != UserQuest.Status.COMPLETED) {
            throw new IllegalStateException("Quest must be COMPLETED before rewarding.");
        }

        Member member = memberRepository.findById(userQuest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        int rewardPoint = userQuest.getQuest().getPoint();

        // 포인트 지급
        member.addPoint(rewardPoint);

        // 포인트 히스토리 저장
        UserPointHistory history = UserPointHistory.builder()
                .memberId(member.getMemberId())
                .parentId(member.getParentId())
                .amount(rewardPoint)
                .type(UserPointHistory.Type.EARN)
                .description("퀘스트 리워드 지급")
                .build();
        userPointHistoryRepository.save(history);

        // 상태 변경
        userQuest.markRewarded();

        log.info("[QuestService] UserQuest {} REWARDED 처리 및 {}포인트 지급 완료", userQuestId, rewardPoint);
    }

    /**
     * 스케줄링용: 매일 Quest 생성 및 유저별 할당
     */
    @Transactional
    public void generateDailyQuests() {
        LocalDate today = LocalDate.now();

        boolean exists = questRepository.existsByStepAndCreatedAt(1, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        if (exists) {
            log.info("[QuestService] 오늘의 Quest가 이미 생성되어 있음 → 스킵");
            return;
        }

        for (int step = 1; step <= 3; step++) {
            questRepository.save(
                    Quest.builder()
                            .step(step)
                            .point(50)
                            .build()
            );
        }

        List<Quest> todayQuests = questRepository.findQuestsByCreatedAtBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay()
        );

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            for (Quest quest : todayQuests) {
                UserQuest userQuest = UserQuest.builder()
                        .memberId(member.getMemberId())
                        .parentId(member.getParentId())
                        .quest(quest)
                        .status(UserQuest.Status.WAITING)
                        .targetDate(today)
                        .build();

                userQuestRepository.save(userQuest);
            }
        }

        log.info("[QuestService] {}명의 모든 유저에게 오늘의 Quest 3개 할당 완료", members.size());
    }
}
