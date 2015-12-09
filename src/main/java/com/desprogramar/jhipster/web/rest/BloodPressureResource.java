package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.BloodPressure;
import com.desprogramar.jhipster.repository.BloodPressureRepository;
import com.desprogramar.jhipster.repository.search.BloodPressureSearchRepository;
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
 * REST controller for managing BloodPressure.
 */
@RestController
@RequestMapping("/api")
public class BloodPressureResource {

    private final Logger log = LoggerFactory.getLogger(BloodPressureResource.class);
        
    @Inject
    private BloodPressureRepository bloodPressureRepository;
    
    @Inject
    private BloodPressureSearchRepository bloodPressureSearchRepository;
    
    /**
     * POST  /bloodPressures -> Create a new bloodPressure.
     */
    @RequestMapping(value = "/bloodPressures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BloodPressure> createBloodPressure(@Valid @RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to save BloodPressure : {}", bloodPressure);
        if (bloodPressure.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bloodPressure", "idexists", "A new bloodPressure cannot already have an ID")).body(null);
        }
        BloodPressure result = bloodPressureRepository.save(bloodPressure);
        bloodPressureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bloodPressures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bloodPressure", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bloodPressures -> Updates an existing bloodPressure.
     */
    @RequestMapping(value = "/bloodPressures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BloodPressure> updateBloodPressure(@Valid @RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to update BloodPressure : {}", bloodPressure);
        if (bloodPressure.getId() == null) {
            return createBloodPressure(bloodPressure);
        }
        BloodPressure result = bloodPressureRepository.save(bloodPressure);
        bloodPressureSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bloodPressure", bloodPressure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bloodPressures -> get all the bloodPressures.
     */
    @RequestMapping(value = "/bloodPressures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BloodPressure>> getAllBloodPressures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BloodPressures");
        Page<BloodPressure> page = bloodPressureRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bloodPressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bloodPressures/:id -> get the "id" bloodPressure.
     */
    @RequestMapping(value = "/bloodPressures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BloodPressure> getBloodPressure(@PathVariable Long id) {
        log.debug("REST request to get BloodPressure : {}", id);
        BloodPressure bloodPressure = bloodPressureRepository.findOne(id);
        return Optional.ofNullable(bloodPressure)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bloodPressures/:id -> delete the "id" bloodPressure.
     */
    @RequestMapping(value = "/bloodPressures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBloodPressure(@PathVariable Long id) {
        log.debug("REST request to delete BloodPressure : {}", id);
        bloodPressureRepository.delete(id);
        bloodPressureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bloodPressure", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bloodPressures/:query -> search for the bloodPressure corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/bloodPressures/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BloodPressure> searchBloodPressures(@PathVariable String query) {
        log.debug("REST request to search BloodPressures for query {}", query);
        return StreamSupport
            .stream(bloodPressureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
