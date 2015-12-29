package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.BloodPressure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the BloodPressure entity.
 */
public interface BloodPressureRepository extends JpaRepository<BloodPressure,Long> {

    @Query("select x from BloodPressure x where x.user.login = ?#{principal.username} ")
	Page<BloodPressure> findAllByUserIsCurrentUser(Pageable pageable);
    
    @Query("select x from BloodPressure x where x.user.login = ?#{principal.username} and x.id = ?1")
    Optional<BloodPressure> findOneByUserIsCurrentUser(Long id);

    @Modifying
    @Transactional
    @Query("delete from BloodPressure x where x.user.login = ?#{principal.username} and x.id = ?1")
    void deleteByUserIsCurrentUser(Long id);

    @Query("select x from BloodPressure x where x.user.login = ?#{principal.username} and x.timestamp between ?1 and ?2 order by x.timestamp")
    List<BloodPressure> findByTimestampAfterCurrentUserOrderByTime(ZonedDateTime desde, ZonedDateTime hasta);
}
