package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Point entity.
 */
public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select point from Point point where point.user.login = ?#{principal.username} order by point.date desc")
    Page<Point> findAllByUserIsCurrentUser(Pageable pageable);

    Page<Point> findAllByOrderByDateDesc(Pageable pageable);

    @Query("select point from Point point where point.user.login = ?#{principal.username} and point.id = ?1")
    Point findOneByUserIsCurrentUser(Long id);

    @Modifying
    @Transactional
    @Query("delete from Point p where p.user.login = ?#{principal.username} and p.id = ?1")
    void deleteByUserIsCurrentUser(Long id);
}
