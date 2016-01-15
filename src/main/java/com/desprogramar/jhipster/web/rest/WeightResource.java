package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.Weight;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.repository.WeightRepository;
import com.desprogramar.jhipster.repository.search.WeightSearchRepository;
import com.desprogramar.jhipster.security.AuthoritiesConstants;
import com.desprogramar.jhipster.security.SecurityUtils;
import com.desprogramar.jhipster.service.PreferenceService;
import com.desprogramar.jhipster.web.rest.dto.WeightByPeriod;
import com.desprogramar.jhipster.web.rest.dto.WeightSearch;
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
 * REST controller for managing Weight.
 */
@RestController
@RequestMapping("/api")
public class WeightResource {

    private final Logger log = LoggerFactory.getLogger(WeightResource.class);
        
    @Inject
    private WeightRepository weightRepository;
    
    @Inject
    private WeightSearchRepository weightSearchRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceService preferenceService;
    
    /**
     * POST  /weights -> Create a new weight.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> createWeight(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to save Weight : {}", weight);
        if (weight.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weight", "idexists", "A new weight cannot already have an ID")).body(null);
        }
        if(weight.getUser() == null) {
            log.debug("No user passed in, using current user");
            weight.setUser(userRepository.findOneByUserIsCurrentUser().get());
        }
        Weight result = weightRepository.save(weight);
        weightSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/weights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weight", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weights -> Updates an existing weight.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> updateWeight(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to update Weight : {}", weight);
        if (weight.getId() == null) {
            return createWeight(weight);
        }
        if(weight.getUser() == null) {
            log.debug("No user passed in, using current user");
            weight.setUser(userRepository.findOneByUserIsCurrentUser().get());
        }
        Weight result = weightRepository.save(weight);
        weightSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weight", weight.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weights -> get all the weights.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Weight>> getAllWeights(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Weights");
        Page<Weight> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = weightRepository.findAll(pageable);
        } else {
            page = weightRepository.findAllByUserIsCurrentUser(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weights");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /weights/:id -> get the "id" weight.
     */
    @RequestMapping(value = "/weights/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> getWeight(@PathVariable Long id) {
        log.debug("REST request to get Weight : {}", id);
        Weight weight = weightRepository.findOne(id);
        if (weight == null || (
        		!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
        		!weight.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	return  new ResponseEntity<>(weight, HttpStatus.OK);
    }

    /**
     * DELETE  /weights/:id -> delete the "id" weight.
     */
    @RequestMapping(value = "/weights/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeight(@PathVariable Long id) {
        log.debug("REST request to delete Weight : {}", id);
        Weight weight = weightRepository.findOne(id);
        if (weight == null || (
        		!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
        		!weight.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        weightRepository.delete(id);
        weightSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weight", id.toString())).build();
    }

    /**
     * POST SEARCH  /_search/weights -> search for the weight corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/weights",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Weight> searchWeights(@RequestBody WeightSearch weightSearch) {
        log.debug("REST request to search Weights for {}", weightSearch);
        AndFilterBuilder filter = new AndFilterBuilder();
		if (weightSearch.getTimestamp() != null) {
			filter.add(FilterBuilders.rangeFilter("timestamp").gte(weightSearch.getTimestampElastic()));
		}
		if (weightSearch.getWeight() != null) {
			filter.add(FilterBuilders.rangeFilter("weight").gte(weightSearch.getWeight()));
		}

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
        	filter.add(FilterBuilders.termFilter("user.login", SecurityUtils.getCurrentUserLogin()));
        }
        QueryBuilder query = filteredQuery(null, filter);
                
        return StreamSupport
            .stream(weightSearchRepository.search(query).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * GET  /w-by-days -> get the "days" del período.
     */
    @RequestMapping(value = "/w-by-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeightByPeriod> getByDays() {
    	Integer days = preferenceService.getUserPreference().getDays();
    	log.debug("REST request to get Weight del período de días: {}", days);
    	ZonedDateTime now = ZonedDateTime.now().withNano(0);
    	ZonedDateTime desde = now.minusDays(days).withHour(0).withMinute(0).withSecond(0);
    	
    	List<Weight> weightList = weightRepository.findAllByTimestampBetweenAndUsersCurrentUserOrderByTime(desde, now);
    	WeightByPeriod lastDaysDTO = new WeightByPeriod(days, weightList);
    	return new ResponseEntity<>(lastDaysDTO, HttpStatus.OK);
    }

    /**
     * GET  /w-by-month/:month -> get all the Weight for a particular month.
     */
    @RequestMapping(value = "/w-by-month/{month}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Weight>> getWByMonth(@PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate month) {
    	ZonedDateTime primerDia = month.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneOffset.UTC);
    	ZonedDateTime ultimoDia = month.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay(ZoneOffset.UTC);
        log.debug("REST request to get a Weight for a particular month between: {} and {}", primerDia, ultimoDia);

		List<Weight> ws = weightRepository.findAllByTimestampBetweenAndUsersCurrentUserOrderByTime(primerDia, ultimoDia);
        return new ResponseEntity<>(ws, HttpStatus.OK);
    }
}
