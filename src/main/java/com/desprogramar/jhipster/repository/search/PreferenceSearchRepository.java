package com.desprogramar.jhipster.repository.search;

import com.desprogramar.jhipster.domain.Preference;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Preference entity.
 */
public interface PreferenceSearchRepository extends ElasticsearchRepository<Preference, Long> {
}
