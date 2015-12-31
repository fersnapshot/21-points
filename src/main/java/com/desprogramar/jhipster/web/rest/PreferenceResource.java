package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.Preference;
import com.desprogramar.jhipster.repository.PreferenceRepository;
import com.desprogramar.jhipster.security.SecurityUtils;
import com.desprogramar.jhipster.service.PreferenceService;
import com.desprogramar.jhipster.web.rest.util.HeaderUtil;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;
import com.desprogramar.jhipster.web.rest.mapper.PreferenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private PreferenceMapper preferenceMapper;
    
    @Autowired
    private PreferenceService preferenceService;

    /**
     * POST  /preferences -> Create a new preference.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferenceDTO> createPreference(@Valid @RequestBody PreferenceDTO preferenceDTO) throws URISyntaxException {
        log.debug("REST request to save Preference : {}", preferenceDTO);
        if (preferenceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("preference", "idexists", "A new preference cannot already have an ID")).body(null);
        }

        // sólo puede quedar uno
        Optional<Preference> preferenceOptional = preferenceRepository.findOneByUserIsCurrentUser();
        if (preferenceOptional.isPresent()) {
            preferenceDTO.setId(preferenceOptional.get().getId());
        }

        Preference preference = preferenceMapper.preferenceDTOToPreference(preferenceDTO);
        preference = preferenceRepository.save(preference);
        PreferenceDTO result = preferenceMapper.preferenceToPreferenceDTO(preference);
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
    public ResponseEntity<PreferenceDTO> updatePreference(@Valid @RequestBody PreferenceDTO preferenceDTO) throws URISyntaxException {
        log.debug("REST request to update Preference : {}", preferenceDTO);
        if (preferenceDTO.getId() == null) {
            return createPreference(preferenceDTO);
        }
        Preference preference = preferenceRepository.findOne(preferenceDTO.getId());
        if (preference == null || !preference.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        preference = preferenceMapper.preferenceDTOToPreference(preferenceDTO);
        preference = preferenceRepository.save(preference);
        PreferenceDTO result = preferenceMapper.preferenceToPreferenceDTO(preference);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("preference", preferenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences -> get all the preferences.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<PreferenceDTO> getAllPreferences() {
        log.debug("REST request to get all Preferences");
        
        Optional<Preference> preferenceOpt = preferenceRepository.findOneByUserIsCurrentUser();
        if (preferenceOpt.isPresent()) {
        	return Arrays.asList(preferenceOpt.map(preferenceMapper::preferenceToPreferenceDTO).get());
        }
        		
        return new ArrayList<>();
    }

    /**
     * GET  /preferences/:id -> get the "id" preference.
     */
    @RequestMapping(value = "/preferences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferenceDTO> getPreference(@PathVariable Long id) {
        log.debug("REST request to get Preference : {}", id);
        Preference preference = preferenceRepository.findOne(id);
        if (preference == null || !preference.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PreferenceDTO preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preference);
        return Optional.ofNullable(preferenceDTO)
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
        Preference preference = preferenceRepository.findOne(id);
        if (preference == null || !preference.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        preferenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preference", id.toString())).build();
    }

    /**
     * GET  /my-preferences -> get the current user's preferences.
     */
    @RequestMapping(value = "/my-preferences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferenceDTO> getUserPreference() {
        log.debug("REST request to get Preference : {}", SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(preferenceService.getUserPreference(), HttpStatus.OK);
    }
    
}
