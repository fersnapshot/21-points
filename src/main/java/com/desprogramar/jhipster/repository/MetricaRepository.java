package com.desprogramar.jhipster.repository;

import com.desprogramar.jhipster.domain.Metrica;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Metrica entity.
 */
public interface MetricaRepository extends JpaRepository<Metrica,Long> {

}
