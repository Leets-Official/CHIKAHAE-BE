package com.leets.chikahae.domain.point.repository;

import com.leets.chikahae.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
