package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.BloodPressure;
import com.desprogramar.jhipster.domain.User;
import com.desprogramar.jhipster.repository.BloodPressureRepository;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.repository.search.BloodPressureSearchRepository;
import com.desprogramar.jhipster.service.PreferenceService;
import com.desprogramar.jhipster.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.*;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * Test class for the BloodPressureResource REST controller.
 *
 * @see BloodPressureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BloodPressureResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_VERSION = 0;
    private static final Integer UPDATED_VERSION = 1;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP);

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;

    @Inject
    private BloodPressureRepository bloodPressureRepository;

    @Inject
    private BloodPressureSearchRepository bloodPressureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBloodPressureMockMvc;

    private BloodPressure bloodPressure;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PreferenceService preferenceService;
    
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodPressureResource bloodPressureResource = new BloodPressureResource();
        ReflectionTestUtils.setField(bloodPressureResource, "bloodPressureSearchRepository", bloodPressureSearchRepository);
        ReflectionTestUtils.setField(bloodPressureResource, "bloodPressureRepository", bloodPressureRepository);
        ReflectionTestUtils.setField(bloodPressureResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(bloodPressureResource, "preferenceService", preferenceService);
        this.restBloodPressureMockMvc = MockMvcBuilders.standaloneSetup(bloodPressureResource)
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
        bloodPressure = new BloodPressure();
//        bloodPressure.setVersion(DEFAULT_VERSION);
        bloodPressure.setTimestamp(DEFAULT_TIMESTAMP);
        bloodPressure.setSystolic(DEFAULT_SYSTOLIC);
        bloodPressure.setDiastolic(DEFAULT_DIASTOLIC);
    }

    @Test
    @Transactional
    public void createBloodPressure() throws Exception {

        this.createUsuarioConRolUser();

        int databaseSizeBeforeCreate = bloodPressureRepository.findAll().size();

        // Create the BloodPressure

        restBloodPressureMockMvc.perform(post("/api/bloodPressures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
                .andExpect(status().isCreated());

        // Validate the BloodPressure in the database
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeCreate + 1);
        BloodPressure testBloodPressure = bloodPressures.get(bloodPressures.size() - 1);
        assertThat(testBloodPressure.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testBloodPressure.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBloodPressure.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBloodPressure.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setTimestamp(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/bloodPressures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
                .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setSystolic(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/bloodPressures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
                .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiastolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setDiastolic(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/bloodPressures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
                .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodPressures() throws Exception {

    	bloodPressure.setUser(createUsuarioConRolUser());

        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

        // Get all the bloodPressures
        restBloodPressureMockMvc.perform(get("/api/bloodPressures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bloodPressure.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
                .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
                .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void getBloodPressure() throws Exception {

    	bloodPressure.setUser(createUsuarioConRolUser());

        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

        // Get the bloodPressure
        restBloodPressureMockMvc.perform(get("/api/bloodPressures/{id}", bloodPressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bloodPressure.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void getNonExistingBloodPressure() throws Exception {
        // Get the bloodPressure
        restBloodPressureMockMvc.perform(get("/api/bloodPressures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodPressure() throws Exception {

    	bloodPressure.setUser(createUsuarioConRolUser());

        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

		int databaseSizeBeforeUpdate = bloodPressureRepository.findAll().size();

        // Update the bloodPressure
//        bloodPressure.setVersion(UPDATED_VERSION);
        bloodPressure.setTimestamp(UPDATED_TIMESTAMP);
        bloodPressure.setSystolic(UPDATED_SYSTOLIC);
        bloodPressure.setDiastolic(UPDATED_DIASTOLIC);

        restBloodPressureMockMvc.perform(put("/api/bloodPressures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
                .andExpect(status().isOk());

        // Validate the BloodPressure in the database
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeUpdate);
        BloodPressure testBloodPressure = bloodPressures.get(bloodPressures.size() - 1);
        assertThat(testBloodPressure.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testBloodPressure.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBloodPressure.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBloodPressure.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void deleteBloodPressure() throws Exception {

    	bloodPressure.setUser(createUsuarioConRolUser());

        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

		int databaseSizeBeforeDelete = bloodPressureRepository.findAll().size();

        // Get the bloodPressure
        restBloodPressureMockMvc.perform(delete("/api/bloodPressures/{id}", bloodPressure.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeDelete - 1);

        this.deleteUsuarioConRolUser();
    }
    
    private void createBloodPressureByMonth() {
		User user = this.createUsuarioConRolUser();
		// this month
    	ZonedDateTime now = ZonedDateTime.now().withNano(0);
		bloodPressure = new BloodPressure(now, 120, 0, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(10), 120, 10, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(20), 120, 20, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(27), 120, 27, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(28), 120, 28, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(29), 120, 29, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(30), 130, 30, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
		bloodPressure = new BloodPressure(now.minusDays(31), 130, 31, user);
		bloodPressureRepository.saveAndFlush(bloodPressure);
    }
    
    @Test
    @Transactional
    public void getBloodPressureForLast30Days() throws Exception {
        createBloodPressureByMonth();
        
        // Get all the blood pressure readings
        restBloodPressureMockMvc.perform(get("/api/bloodPressures"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(8)));
        
        // Get the blood pressure readings for the last 30 days
        restBloodPressureMockMvc.perform(get("/api/bp-by-days"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.period").value(30))
            .andExpect(jsonPath("$.readings.[*].systolic").value(hasItem(120)))
        	.andExpect(jsonPath("$.readings.[*].diastolic").value(hasItems(0, 10, 20, 27, 28, 29)))
        	.andExpect(jsonPath("$.readings.[*].diastolic").value(hasItem(30)));
//        	.andExpect(jsonPath("$.readings.[*].diastolic").value(hasItem(31)));	// ERROR minusDays(31)

        this.deleteUsuarioConRolUser();
    }

}
