package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Preference;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Preference entity.
 */
public interface PreferenceRepository extends JpaRepository<Preference,Long> {

    @Query("select x from Preference x where x.user.login = ?#{principal.username}")
    Optional<Preference> findOneByUserIsCurrentUser();

    @Transactional
    Long deleteByUserLogin(String login);
}
