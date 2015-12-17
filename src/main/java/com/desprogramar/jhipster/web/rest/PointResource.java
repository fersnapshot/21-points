package com.desprogramar.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.desprogramar.jhipster.domain.Point;
import com.desprogramar.jhipster.repository.PointRepository;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.repository.search.PointSearchRepository;
import com.desprogramar.jhipster.security.AuthoritiesConstants;
import com.desprogramar.jhipster.security.SecurityUtils;
import com.desprogramar.jhipster.web.rest.util.HeaderUtil;
import com.desprogramar.jhipster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing Point.
 */
@RestController
@RequestMapping("/api")
public class PointResource {

    private final Logger log = LoggerFactory.getLogger(PointResource.class);

    @Inject
    private PointRepository pointRepository;

    @Inject
    private PointSearchRepository pointSearchRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /points -> Create a new point.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> createPoint(@Valid @RequestBody Point point) throws URISyntaxException {
        log.debug("REST request to save Point : {}", point);
        if (point.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("point", "idexists", "A new point cannot already have an ID")).body(null);
        }
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || point.getUser() == null) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            point.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        Point result = pointRepository.save(point);
        pointSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("point", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /points -> Updates an existing point.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> updatePoint(@Valid @RequestBody Point point) throws URISyntaxException {
        log.debug("REST request to update Point : {}", point);
        if (point.getId() == null) {
            return createPoint(point);
        }
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || point.getUser() == null) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            point.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        Point result = pointRepository.save(point);
        pointSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("point", point.getId().toString()))
            .body(result);
    }

    /**
     * GET  /points -> get all the points.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Point>> getAllPoints(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Points");
        Page<Point> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = pointRepository.findAllByOrderByDateDesc(pageable);
        } else {
            page = pointRepository.findAllByUserIsCurrentUser(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /points/:id -> get the "id" point.
     */
    @RequestMapping(value = "/points/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> getPoint(@PathVariable Long id) {
        log.debug("REST request to get Point : {}", id);
        Point point = pointRepository.findOne(id);
        if (point != null &&
            !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
            !point.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("point", "idNoExists", "El ID '"+id+"' no existe!")).body(null);
                // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return Optional.ofNullable(point)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /points/:id -> delete the "id" point.
     */
    @RequestMapping(value = "/points/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
        log.debug("REST request to delete Point : {}", id);
        Point point = pointRepository.findOne(id);
        if (point != null &&
            !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
            !point.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("point", "idNoExists", "El ID '"+id+"' no existe!")).build();
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pointRepository.delete(id);
        pointSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("point", id.toString())).build();
    }

    /**
     * SEARCH  /_search/points/:query -> search for the point corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/points/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Point> searchPoints(@PathVariable String query) {
        log.debug("REST request to search Points for query {}", query);
        return StreamSupport
            .stream(pointSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
