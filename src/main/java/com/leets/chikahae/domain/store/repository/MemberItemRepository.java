package com.leets.chikahae.domain.store.repository;

import com.leets.chikahae.domain.store.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

}
