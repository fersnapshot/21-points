package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.BloodPressure;
import com.desprogramar.jhipster.repository.BloodPressureRepository;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.repository.search.BloodPressureSearchRepository;
import com.desprogramar.jhipster.security.AuthoritiesConstants;
import com.desprogramar.jhipster.security.SecurityUtils;
import com.desprogramar.jhipster.service.PreferenceService;
import com.desprogramar.jhipster.web.rest.dto.BloodPressureByPeriod;
import com.desprogramar.jhipster.web.rest.dto.BloodPressureSearch;
import com.desprogramar.jhipster.web.rest.util.HeaderUtil;
import com.desprogramar.jhipster.web.rest.util.PaginationUtil;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
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
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PreferenceService preferenceService;
    
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
        if (bloodPressure.getUser() == null) {
            log.debug("No user passed in, using current user");
            bloodPressure.setUser(userRepository.findOneByUserIsCurrentUser().get());
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
        if (bloodPressure.getUser() == null) {
            log.debug("No user passed in, using current user");
            bloodPressure.setUser(userRepository.findOneByUserIsCurrentUser().get());
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
        Page<BloodPressure> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = bloodPressureRepository.findAll(pageable);
        } else {
            page = bloodPressureRepository.findAllByUserIsCurrentUser(pageable);
        }
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
        if (bloodPressure == null || (
        		!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
            	!bloodPressure.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) ) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(id.toString(), "idNoExists", "")).body(null);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	return  new ResponseEntity<>(bloodPressure, HttpStatus.OK);
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
        BloodPressure bloodPressure = bloodPressureRepository.findOne(id);
        if (bloodPressure != null &&
        		!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
        		!bloodPressure.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
//        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bloodPressure", "idNoExiste", "El ID '"+id+"' no existe!")).build();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bloodPressureRepository.delete(id);
        bloodPressureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bloodPressure", id.toString())).build();
    }

    /**
     * POST SEARCH  /_search/bloodPressures -> search for the bloodPressure corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/bloodPressures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BloodPressure> searchBloodPressures(@RequestBody BloodPressureSearch search) {
        log.debug("REST request to search BloodPressures for {}", search);
        AndFilterBuilder filter = new AndFilterBuilder();
		if (search.getTimestamp() != null) {
			filter.add(FilterBuilders.rangeFilter("timestamp").gte(search.getTimestampElastic()));
		}
		if (search.getSystolic() != null) {
			filter.add(FilterBuilders.rangeFilter("systolic").gte(search.getSystolic()));
		}
		if (search.getDiastolic() != null) {
			filter.add(FilterBuilders.rangeFilter("diastolic").gte(search.getDiastolic()));
		}

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
        	filter.add(FilterBuilders.termFilter("user.login", SecurityUtils.getCurrentUserLogin()));
        }
        QueryBuilder query = filteredQuery(null, filter);
                
        return StreamSupport
            .stream(bloodPressureSearchRepository.search(query).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * GET  /bp-by-days -> get the "days" del período.
     */
    @RequestMapping(value = "/bp-by-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BloodPressureByPeriod> getByDays() {
    	Integer days = preferenceService.getUserPreference().getDays();
        log.debug("REST request to get BloodPressure del período de días: {}", days);
    	ZonedDateTime now = ZonedDateTime.now().withNano(0);
    	ZonedDateTime desde = now.minusDays(days).withHour(0).withMinute(0).withSecond(0);
    	
    	List<BloodPressure> bloodList = bloodPressureRepository.findAllByTimestampBetweenAndUsersCurrentUserOrderByTime(desde, now);
    	BloodPressureByPeriod lastDaysDTO = new BloodPressureByPeriod(days, bloodList);
    	return new ResponseEntity<>(lastDaysDTO, HttpStatus.OK);
    }

    /**
     * GET  /bp-by-month/:month -> get all the bp for a particular month.
     */
    @RequestMapping(value = "/bp-by-month/{month}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BloodPressure>> getBPByMonth(@PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate month) {
    	ZonedDateTime primerDia = month.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneOffset.UTC);
    	ZonedDateTime ultimoDia = month.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay(ZoneOffset.UTC);
        log.debug("REST request to get a BloodPressure for a particular month between: {} and {}", primerDia, ultimoDia);

		List<BloodPressure> bps = bloodPressureRepository.findAllByTimestampBetweenAndUsersCurrentUserOrderByTime(primerDia, ultimoDia);
        return new ResponseEntity<>(bps, HttpStatus.OK);
    }
    
}
