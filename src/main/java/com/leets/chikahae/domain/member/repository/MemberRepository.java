package com.leets.chikahae.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.chikahae.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
