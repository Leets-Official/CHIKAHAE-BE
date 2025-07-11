package com.leets.chikahae.domain.parent.repository;

import com.leets.chikahae.domain.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByKakaoId(String kakaoId);
}
