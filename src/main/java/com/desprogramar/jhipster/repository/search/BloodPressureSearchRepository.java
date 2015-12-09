package com.desprogramar.jhipster.repository.search;

import com.desprogramar.jhipster.domain.BloodPressure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BloodPressure entity.
 */
public interface BloodPressureSearchRepository extends ElasticsearchRepository<BloodPressure, Long> {
}
