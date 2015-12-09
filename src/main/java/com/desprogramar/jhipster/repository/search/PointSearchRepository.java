package com.desprogramar.jhipster.repository.search;

import com.desprogramar.jhipster.domain.Point;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Point entity.
 */
public interface PointSearchRepository extends ElasticsearchRepository<Point, Long> {
}
