package com.leets.chikahae.domain.item.repository;

import com.leets.chikahae.domain.item.entity.Item;
import com.leets.chikahae.domain.item.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

}
