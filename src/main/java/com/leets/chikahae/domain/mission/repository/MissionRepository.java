package com.leets.chikahae.domain.mission.repository;

import com.leets.chikahae.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByCode(Mission.MissionCode code);
}

