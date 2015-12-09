package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.Preference;
import com.desprogramar.jhipster.repository.PreferenceRepository;
import com.desprogramar.jhipster.repository.search.PreferenceSearchRepository;
import com.desprogramar.jhipster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Preference.
 */
@RestController
@RequestMapping("/api")
public class PreferenceResource {

    private final Logger log = LoggerFactory.getLogger(PreferenceResource.class);
        
    @Inject
    private PreferenceRepository preferenceRepository;
    
    @Inject
    private PreferenceSearchRepository preferenceSearchRepository;
    
    /**
     * POST  /preferences -> Create a new preference.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preference> createPreference(@Valid @RequestBody Preference preference) throws URISyntaxException {
        log.debug("REST request to save Preference : {}", preference);
        if (preference.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("preference", "idexists", "A new preference cannot already have an ID")).body(null);
        }
        Preference result = preferenceRepository.save(preference);
        preferenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("preference", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferences -> Updates an existing preference.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preference> updatePreference(@Valid @RequestBody Preference preference) throws URISyntaxException {
        log.debug("REST request to update Preference : {}", preference);
        if (preference.getId() == null) {
            return createPreference(preference);
        }
        Preference result = preferenceRepository.save(preference);
        preferenceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("preference", preference.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences -> get all the preferences.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preference> getAllPreferences(@RequestParam(required = false) String filter) {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all Preferences where user is null");
            return StreamSupport
                .stream(preferenceRepository.findAll().spliterator(), false)
                .filter(preference -> preference.getUser() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Preferences");
        return preferenceRepository.findAll();
            }

    /**
     * GET  /preferences/:id -> get the "id" preference.
     */
    @RequestMapping(value = "/preferences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preference> getPreference(@PathVariable Long id) {
        log.debug("REST request to get Preference : {}", id);
        Preference preference = preferenceRepository.findOne(id);
        return Optional.ofNullable(preference)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /preferences/:id -> delete the "id" preference.
     */
    @RequestMapping(value = "/preferences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        log.debug("REST request to delete Preference : {}", id);
        preferenceRepository.delete(id);
        preferenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preference", id.toString())).build();
    }

    /**
     * SEARCH  /_search/preferences/:query -> search for the preference corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/preferences/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preference> searchPreferences(@PathVariable String query) {
        log.debug("REST request to search Preferences for query {}", query);
        return StreamSupport
            .stream(preferenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
