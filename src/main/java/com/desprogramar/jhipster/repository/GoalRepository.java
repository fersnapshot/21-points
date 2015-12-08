package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Goal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Goal entity.
 */
public interface GoalRepository extends JpaRepository<Goal,Long> {

    @Query("select goal from Goal goal where goal.user.login = ?#{principal.username}")
    List<Goal> findByUserIsCurrentUser();

}
