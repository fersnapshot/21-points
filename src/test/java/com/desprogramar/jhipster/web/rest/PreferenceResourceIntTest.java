package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.Preference;
import com.desprogramar.jhipster.domain.User;
import com.desprogramar.jhipster.repository.PreferenceRepository;
import com.desprogramar.jhipster.service.UserService;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;
import com.desprogramar.jhipster.web.rest.mapper.PreferenceMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final Integer DEFAULT_WEEKLY_GOAL = 10;
    private static final Integer UPDATED_WEEKLY_GOAL = 11;

    private static final Units DEFAULT_WEIGHT_UNITS = Units.kg;
    private static final Units UPDATED_WEIGHT_UNITS = Units.lg;

    @Inject
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPreferenceMockMvc;

    private Preference preference;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PreferenceResource preferenceResource = new PreferenceResource();
        ReflectionTestUtils.setField(preferenceResource, "preferenceRepository", preferenceRepository);
        ReflectionTestUtils.setField(preferenceResource, "preferenceMapper", preferenceMapper);
        this.restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();

    }

    private User createUsuarioConRolUser() {
        User user = userService.createUserInformation(TestUtil.USUARIO_LOGIN, "password", "Juan", "Palomo", "palomo@desprogramar.com", "es");

        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(
            new org.springframework.security.core.userdetails.User(
                user.getLogin(), // login de usuario en base de datos (nuestro User)
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER")
            ),
            "user");

        SecurityContextHolder.getContext().setAuthentication(userAuth);
        return user;
    }

    private void deleteUsuarioConRolUser() {
        userService.deleteUserInformation(TestUtil.USUARIO_LOGIN);
    }

    @Before
    public void initTest() {
        preference = new Preference();
        preference.setWeeklyGoal(DEFAULT_WEEKLY_GOAL);
        preference.setWeightUnits(DEFAULT_WEIGHT_UNITS);
    }

    @Test
    @Transactional
    public void createPreference() throws Exception {

        this.createUsuarioConRolUser();

        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preference);

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
                .andExpect(status().isCreated());

        // Validate the Preference in the database
        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeCreate + 1);
        Preference testPreference = preferences.get(preferences.size() - 1);
        assertThat(testPreference.getWeeklyGoal()).isEqualTo(DEFAULT_WEEKLY_GOAL);
        assertThat(testPreference.getWeightUnits()).isEqualTo(DEFAULT_WEIGHT_UNITS);

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void checkWeeklyGoalIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferenceRepository.findAll().size();
        // set the field null
        preference.setWeeklyGoal(null);

        // Create the Preference, which fails.
        PreferenceDTO preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preference);

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
                .andExpect(status().isBadRequest());

        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferenceRepository.findAll().size();
        // set the field null
        preference.setWeightUnits(null);

        // Create the Preference, which fails.
        PreferenceDTO preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preference);

        restPreferenceMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
                .andExpect(status().isBadRequest());

        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
    	
    	preference.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferences
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
                .andExpect(jsonPath("$.[*].weeklyGoal").value(hasItem(DEFAULT_WEEKLY_GOAL)))
                .andExpect(jsonPath("$.[*].weightUnits").value(hasItem(DEFAULT_WEIGHT_UNITS.toString())));
        
        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void getPreference() throws Exception {
    	
    	preference.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", preference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(preference.getId().intValue()))
            .andExpect(jsonPath("$.weeklyGoal").value(DEFAULT_WEEKLY_GOAL))
            .andExpect(jsonPath("$.weightUnits").value(DEFAULT_WEIGHT_UNITS.toString()));
        
        this.deleteUsuarioConRolUser();
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

        preference.setUser(createUsuarioConRolUser());

        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

		int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Update the preference
        preference.setWeeklyGoal(UPDATED_WEEKLY_GOAL);
        preference.setWeightUnits(UPDATED_WEIGHT_UNITS);
        PreferenceDTO preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preference);

        restPreferenceMockMvc.perform(put("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
                .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferences = preferenceRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeUpdate);
        Preference testPreference = preferences.get(preferences.size() - 1);
        assertThat(testPreference.getWeeklyGoal()).isEqualTo(UPDATED_WEEKLY_GOAL);
        assertThat(testPreference.getWeightUnits()).isEqualTo(UPDATED_WEIGHT_UNITS);

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void deletePreference() throws Exception {

        preference.setUser(createUsuarioConRolUser());

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

        this.deleteUsuarioConRolUser();
    }
}
