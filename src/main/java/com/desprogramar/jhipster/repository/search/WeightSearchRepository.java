package com.desprogramar.jhipster.repository.search;

import com.desprogramar.jhipster.domain.Weight;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Weight entity.
 */
public interface WeightSearchRepository extends ElasticsearchRepository<Weight, Long> {
}
