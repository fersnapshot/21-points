package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Preference;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Preference entity.
 */
public interface PreferenceRepository extends JpaRepository<Preference,Long> {

}
