package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.Metrica;
import com.desprogramar.jhipster.repository.MetricaRepository;
import com.desprogramar.jhipster.repository.search.MetricaSearchRepository;
import com.desprogramar.jhipster.web.rest.util.HeaderUtil;
import com.desprogramar.jhipster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Metrica.
 */
@RestController
@RequestMapping("/api")
public class MetricaResource {

    private final Logger log = LoggerFactory.getLogger(MetricaResource.class);
        
    @Inject
    private MetricaRepository metricaRepository;
    
    @Inject
    private MetricaSearchRepository metricaSearchRepository;
    
    /**
     * POST  /metricas -> Create a new metrica.
     */
    @RequestMapping(value = "/metricas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metrica> createMetrica(@Valid @RequestBody Metrica metrica) throws URISyntaxException {
        log.debug("REST request to save Metrica : {}", metrica);
        if (metrica.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("metrica", "idexists", "A new metrica cannot already have an ID")).body(null);
        }
        Metrica result = metricaRepository.save(metrica);
        metricaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/metricas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("metrica", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metricas -> Updates an existing metrica.
     */
    @RequestMapping(value = "/metricas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metrica> updateMetrica(@Valid @RequestBody Metrica metrica) throws URISyntaxException {
        log.debug("REST request to update Metrica : {}", metrica);
        if (metrica.getId() == null) {
            return createMetrica(metrica);
        }
        Metrica result = metricaRepository.save(metrica);
        metricaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("metrica", metrica.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metricas -> get all the metricas.
     */
    @RequestMapping(value = "/metricas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Metrica>> getAllMetricas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Metricas");
        Page<Metrica> page = metricaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metricas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metricas/:id -> get the "id" metrica.
     */
    @RequestMapping(value = "/metricas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metrica> getMetrica(@PathVariable Long id) {
        log.debug("REST request to get Metrica : {}", id);
        Metrica metrica = metricaRepository.findOne(id);
        return Optional.ofNullable(metrica)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /metricas/:id -> delete the "id" metrica.
     */
    @RequestMapping(value = "/metricas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMetrica(@PathVariable Long id) {
        log.debug("REST request to delete Metrica : {}", id);
        metricaRepository.delete(id);
        metricaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("metrica", id.toString())).build();
    }

    /**
     * SEARCH  /_search/metricas/:query -> search for the metrica corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/metricas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Metrica> searchMetricas(@PathVariable String query) {
        log.debug("REST request to search Metricas for query {}", query);
        return StreamSupport
            .stream(metricaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
