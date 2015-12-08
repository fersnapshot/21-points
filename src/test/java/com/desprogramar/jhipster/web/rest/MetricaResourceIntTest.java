package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.Metrica;
import com.desprogramar.jhipster.repository.MetricaRepository;
import com.desprogramar.jhipster.repository.search.MetricaSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MetricaResource REST controller.
 *
 * @see MetricaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MetricaResourceIntTest {


    private static final Integer DEFAULT_VERSION = 0;
    private static final Integer UPDATED_VERSION = 1;
    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_AMOUNT = "AAAAA";
    private static final String UPDATED_AMOUNT = "BBBBB";

    @Inject
    private MetricaRepository metricaRepository;

    @Inject
    private MetricaSearchRepository metricaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMetricaMockMvc;

    private Metrica metrica;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetricaResource metricaResource = new MetricaResource();
        ReflectionTestUtils.setField(metricaResource, "metricaSearchRepository", metricaSearchRepository);
        ReflectionTestUtils.setField(metricaResource, "metricaRepository", metricaRepository);
        this.restMetricaMockMvc = MockMvcBuilders.standaloneSetup(metricaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metrica = new Metrica();
//        metrica.setVersion(DEFAULT_VERSION);
        metrica.setName(DEFAULT_NAME);
        metrica.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createMetrica() throws Exception {
        int databaseSizeBeforeCreate = metricaRepository.findAll().size();

        // Create the Metrica

        restMetricaMockMvc.perform(post("/api/metricas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metrica)))
                .andExpect(status().isCreated());

        // Validate the Metrica in the database
        List<Metrica> metricas = metricaRepository.findAll();
        assertThat(metricas).hasSize(databaseSizeBeforeCreate + 1);
        Metrica testMetrica = metricas.get(metricas.size() - 1);
        assertThat(testMetrica.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testMetrica.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMetrica.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricaRepository.findAll().size();
        // set the field null
        metrica.setName(null);

        // Create the Metrica, which fails.

        restMetricaMockMvc.perform(post("/api/metricas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metrica)))
                .andExpect(status().isBadRequest());

        List<Metrica> metricas = metricaRepository.findAll();
        assertThat(metricas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricaRepository.findAll().size();
        // set the field null
        metrica.setAmount(null);

        // Create the Metrica, which fails.

        restMetricaMockMvc.perform(post("/api/metricas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metrica)))
                .andExpect(status().isBadRequest());

        List<Metrica> metricas = metricaRepository.findAll();
        assertThat(metricas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetricas() throws Exception {
        // Initialize the database
        metricaRepository.saveAndFlush(metrica);

        // Get all the metricas
        restMetricaMockMvc.perform(get("/api/metricas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metrica.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.toString())));
    }

    @Test
    @Transactional
    public void getMetrica() throws Exception {
        // Initialize the database
        metricaRepository.saveAndFlush(metrica);

        // Get the metrica
        restMetricaMockMvc.perform(get("/api/metricas/{id}", metrica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metrica.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetrica() throws Exception {
        // Get the metrica
        restMetricaMockMvc.perform(get("/api/metricas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetrica() throws Exception {
        // Initialize the database
        metricaRepository.saveAndFlush(metrica);

		int databaseSizeBeforeUpdate = metricaRepository.findAll().size();

        // Update the metrica
//        metrica.setVersion(UPDATED_VERSION);
        metrica.setName(UPDATED_NAME);
        metrica.setAmount(UPDATED_AMOUNT);

        restMetricaMockMvc.perform(put("/api/metricas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metrica)))
                .andExpect(status().isOk());

        // Validate the Metrica in the database
        List<Metrica> metricas = metricaRepository.findAll();
        assertThat(metricas).hasSize(databaseSizeBeforeUpdate);
        Metrica testMetrica = metricas.get(metricas.size() - 1);
        assertThat(testMetrica.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMetrica.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMetrica.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteMetrica() throws Exception {
        // Initialize the database
        metricaRepository.saveAndFlush(metrica);

		int databaseSizeBeforeDelete = metricaRepository.findAll().size();

        // Get the metrica
        restMetricaMockMvc.perform(delete("/api/metricas/{id}", metrica.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Metrica> metricas = metricaRepository.findAll();
        assertThat(metricas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
