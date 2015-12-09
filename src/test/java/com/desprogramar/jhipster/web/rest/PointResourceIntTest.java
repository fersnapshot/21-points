package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.Point;
import com.desprogramar.jhipster.repository.PointRepository;
import com.desprogramar.jhipster.repository.search.PointSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PointResource REST controller.
 *
 * @see PointResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PointResourceIntTest {


    private static final Integer DEFAULT_VERSION = 0;
    private static final Integer UPDATED_VERSION = 1;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EXERCISE = 1;
    private static final Integer UPDATED_EXERCISE = 2;

    private static final Integer DEFAULT_MEALS = 1;
    private static final Integer UPDATED_MEALS = 2;

    private static final Integer DEFAULT_ALCOHOL = 1;
    private static final Integer UPDATED_ALCOHOL = 2;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private PointRepository pointRepository;

    @Inject
    private PointSearchRepository pointSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPointMockMvc;

    private Point point;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PointResource pointResource = new PointResource();
        ReflectionTestUtils.setField(pointResource, "pointSearchRepository", pointSearchRepository);
        ReflectionTestUtils.setField(pointResource, "pointRepository", pointRepository);
        this.restPointMockMvc = MockMvcBuilders.standaloneSetup(pointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        point = new Point();
//        point.setVersion(DEFAULT_VERSION);
        point.setDate(DEFAULT_DATE);
        point.setExercise(DEFAULT_EXERCISE);
        point.setMeals(DEFAULT_MEALS);
        point.setAlcohol(DEFAULT_ALCOHOL);
        point.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createPoint() throws Exception {
        int databaseSizeBeforeCreate = pointRepository.findAll().size();

        // Create the Point

        restPointMockMvc.perform(post("/api/points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(point)))
                .andExpect(status().isCreated());

        // Validate the Point in the database
        List<Point> points = pointRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeCreate + 1);
        Point testPoint = points.get(points.size() - 1);
        assertThat(testPoint.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testPoint.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoint.getExercise()).isEqualTo(DEFAULT_EXERCISE);
        assertThat(testPoint.getMeals()).isEqualTo(DEFAULT_MEALS);
        assertThat(testPoint.getAlcohol()).isEqualTo(DEFAULT_ALCOHOL);
        assertThat(testPoint.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointRepository.findAll().size();
        // set the field null
        point.setDate(null);

        // Create the Point, which fails.

        restPointMockMvc.perform(post("/api/points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(point)))
                .andExpect(status().isBadRequest());

        List<Point> points = pointRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPoints() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the points
        restPointMockMvc.perform(get("/api/points?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(point.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE)))
                .andExpect(jsonPath("$.[*].meals").value(hasItem(DEFAULT_MEALS)))
                .andExpect(jsonPath("$.[*].alcohol").value(hasItem(DEFAULT_ALCOHOL)))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getPoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get the point
        restPointMockMvc.perform(get("/api/points/{id}", point.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(point.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.exercise").value(DEFAULT_EXERCISE))
            .andExpect(jsonPath("$.meals").value(DEFAULT_MEALS))
            .andExpect(jsonPath("$.alcohol").value(DEFAULT_ALCOHOL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPoint() throws Exception {
        // Get the point
        restPointMockMvc.perform(get("/api/points/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

		int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Update the point
//        point.setVersion(UPDATED_VERSION);
        point.setDate(UPDATED_DATE);
        point.setExercise(UPDATED_EXERCISE);
        point.setMeals(UPDATED_MEALS);
        point.setAlcohol(UPDATED_ALCOHOL);
        point.setNotes(UPDATED_NOTES);

        restPointMockMvc.perform(put("/api/points")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(point)))
                .andExpect(status().isOk());

        // Validate the Point in the database
        List<Point> points = pointRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeUpdate);
        Point testPoint = points.get(points.size() - 1);
        assertThat(testPoint.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testPoint.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoint.getExercise()).isEqualTo(UPDATED_EXERCISE);
        assertThat(testPoint.getMeals()).isEqualTo(UPDATED_MEALS);
        assertThat(testPoint.getAlcohol()).isEqualTo(UPDATED_ALCOHOL);
        assertThat(testPoint.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deletePoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

		int databaseSizeBeforeDelete = pointRepository.findAll().size();

        // Get the point
        restPointMockMvc.perform(delete("/api/points/{id}", point.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Point> points = pointRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeDelete - 1);
    }
}
