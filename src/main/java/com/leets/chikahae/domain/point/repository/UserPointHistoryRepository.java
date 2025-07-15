package com.leets.chikahae.domain.point.repository;

import com.leets.chikahae.domain.point.entity.UserPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPointHistoryRepository extends JpaRepository<UserPointHistory, Long> {

    /**
     * 특정 유저의 포인트 히스토리 조회
     */
    List<UserPointHistory> findByMember_MemberId(Long memberId);
}
