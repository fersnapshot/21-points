package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.Preference;
import com.desprogramar.jhipster.repository.PreferenceRepository;
import com.desprogramar.jhipster.repository.search.PreferenceSearchRepository;

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

import com.desprogramar.jhipster.domain.enumeration.Units;

/**
 * Test class for the PreferenceResource REST controller.
 *
 * @see PreferenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PreferenceResourceIntTest {


    private static final Integer DEFAULT_VERSION = 0;
    private static final Integer UPDATED_VERSION = 1;

    private static final Integer DEFAULT_WEEKLY_GOAL = 10;
    private static final Integer UPDATED_WEEKLY_GOAL = 11;


    private static final Units DEFAULT_WEIGHT_UNITS = Units.kg;
    private static final Units UPDATED_WEIGHT_UNITS = Units.lg;

    @Inject
    private PreferenceRepository preferenceRepository;

    @Inject
    private PreferenceSearchRepository preferenceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPreferenceMockMvc;

    private Preference preference;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PreferenceResource preferenceResource = new PreferenceResource();
        ReflectionTestUtils.setField(preferenceResource, "preferenceSearchRepository", preferenceSearchRepository);
        ReflectionTestUtils.setField(preferenceResource, "preferenceRepository", preferenceRepository);
        this.restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        preference = new Preference();
//        preference.setVersion(DEFAULT_VERSION);
        preference.setWeekly_goal(DEFAULT_WEEKLY_GOAL);
        preference.setWeight_units(DEFAULT_WEIGHT_UNITS);
    }

    @Test
    @Transactional
    public void createPreference() throws Exception {
        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preference)))
                .andExpect(status().isCreated());

        // Validate the Preference in the database
        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeCreate + 1);
        Preference testPreference = preferences.get(preferences.size() - 1);
        assertThat(testPreference.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testPreference.getWeekly_goal()).isEqualTo(DEFAULT_WEEKLY_GOAL);
        assertThat(testPreference.getWeight_units()).isEqualTo(DEFAULT_WEIGHT_UNITS);
    }

    @Test
    @Transactional
    public void checkWeekly_goalIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferenceRepository.findAll().size();
        // set the field null
        preference.setWeekly_goal(null);

        // Create the Preference, which fails.

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preference)))
                .andExpect(status().isBadRequest());

        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeight_unitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferenceRepository.findAll().size();
        // set the field null
        preference.setWeight_units(null);

        // Create the Preference, which fails.

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preference)))
                .andExpect(status().isBadRequest());

        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferences
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
                .andExpect(jsonPath("$.[*].weekly_goal").value(hasItem(DEFAULT_WEEKLY_GOAL)))
                .andExpect(jsonPath("$.[*].weight_units").value(hasItem(DEFAULT_WEIGHT_UNITS.toString())));
    }

    @Test
    @Transactional
    public void getPreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", preference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(preference.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.weekly_goal").value(DEFAULT_WEEKLY_GOAL))
            .andExpect(jsonPath("$.weight_units").value(DEFAULT_WEIGHT_UNITS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPreference() throws Exception {
        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

		int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Update the preference
//        preference.setVersion(UPDATED_VERSION);
        preference.setWeekly_goal(UPDATED_WEEKLY_GOAL);
        preference.setWeight_units(UPDATED_WEIGHT_UNITS);

        restPreferenceMockMvc.perform(put("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preference)))
                .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeUpdate);
        Preference testPreference = preferences.get(preferences.size() - 1);
        assertThat(testPreference.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testPreference.getWeekly_goal()).isEqualTo(UPDATED_WEEKLY_GOAL);
        assertThat(testPreference.getWeight_units()).isEqualTo(UPDATED_WEIGHT_UNITS);
    }

    @Test
    @Transactional
    public void deletePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

		int databaseSizeBeforeDelete = preferenceRepository.findAll().size();

        // Get the preference
        restPreferenceMockMvc.perform(delete("/api/preferences/{id}", preference.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
