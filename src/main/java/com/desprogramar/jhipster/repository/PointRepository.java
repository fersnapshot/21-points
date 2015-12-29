package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Point entity.
 */
public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select x from Point x where x.user.login = ?#{principal.username} ")
    Page<Point> findAllByUserIsCurrentUser(Pageable pageable);

    @Query("select x from Point x where x.user.login = ?#{principal.username} and x.id = ?1")
    Optional<Point> findOneByUserIsCurrentUser(Long id);

    @Modifying
    @Transactional
    @Query("delete from Point x where x.user.login = ?#{principal.username} and x.id = ?1")
    void deleteByUserIsCurrentUser(Long id);

    @Query("select x from Point x where x.user.login = ?#{principal.username} and x.date between ?1 and ?2")
    List<Point> findAllByDateBetweenAndUsersCurrentUser(LocalDate firstDate, LocalDate secondDate);
}
