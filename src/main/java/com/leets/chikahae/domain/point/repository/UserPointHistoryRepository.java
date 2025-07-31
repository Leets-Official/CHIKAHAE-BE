package com.leets.chikahae.domain.point.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.point.entity.UserPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserPointHistoryRepository extends JpaRepository<UserPointHistory, Long> {

    /**
     * 특정 유저의 포인트 히스토리 조회
     */
    List<UserPointHistory> findByMember_MemberId(Long memberId);

    // 해당 날짜에 quiz에 대한 point 이력이 있으면 오류 반환
    boolean existsByMemberAndDateAndTypeAndDescription(
            Member member,
            LocalDate date,
            UserPointHistory.Type type,
            String description
    );}
