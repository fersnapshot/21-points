package com.desprogramar.jhipster.repository.search;

import com.desprogramar.jhipster.domain.Metrica;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Metrica entity.
 */
public interface MetricaSearchRepository extends ElasticsearchRepository<Metrica, Long> {
}
