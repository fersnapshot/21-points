package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Point;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Point entity.
 */
public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select point from Point point where point.user.login = ?#{principal.username}")
    List<Point> findByUserIsCurrentUser();

}
