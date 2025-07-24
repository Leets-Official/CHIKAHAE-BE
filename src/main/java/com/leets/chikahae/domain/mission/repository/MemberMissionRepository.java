package com.leets.chikahae.domain.mission.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.mission.entity.MemberMission;
import com.leets.chikahae.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {
    Optional<MemberMission> findByMemberAndMission(Member member, Mission mission);
}
