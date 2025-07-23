package com.leets.chikahae.domain.store.repository;

import com.leets.chikahae.domain.store.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

    // 특정 회원이 소유한 아이템 목록 조회
    List<MemberItem> findByMember_MemberId(Long memberId);
}
