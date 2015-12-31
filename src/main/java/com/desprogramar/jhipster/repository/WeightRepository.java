package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Weight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Weight entity.
 */
public interface WeightRepository extends JpaRepository<Weight,Long> {

    @Query("select x from Weight x where x.user.login = ?#{principal.username}")
    List<Weight> findAllByUserIsCurrentUser();

    @Query("select x from Weight x where x.user.login = ?#{principal.username} ")
	Page<Weight> findAllByUserIsCurrentUser(Pageable pageable);
    
    @Query("select x from Weight x where x.user.login = ?#{principal.username} and x.id = ?1")
    Optional<Weight> findOneByUserIsCurrentUser(Long id);

    @Modifying
    @Transactional
    @Query("delete from Weight x where x.user.login = ?#{principal.username} and x.id = ?1")
    void deleteByUserIsCurrentUser(Long id);

    @Query("select x from Weight x where x.user.login = ?#{principal.username} and x.timestamp between ?1 and ?2 order by x.timestamp")
    List<Weight> findByTimestampAfterCurrentUserOrderByTime(ZonedDateTime desde, ZonedDateTime hasta);
}
