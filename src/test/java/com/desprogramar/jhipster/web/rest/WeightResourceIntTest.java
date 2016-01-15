package com.desprogramar.jhipster.web.rest;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.domain.User;
import com.desprogramar.jhipster.domain.Weight;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.repository.WeightRepository;
import com.desprogramar.jhipster.repository.search.WeightSearchRepository;
import com.desprogramar.jhipster.service.PreferenceService;
import com.desprogramar.jhipster.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

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
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WeightResource REST controller.
 *
 * @see WeightResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WeightResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_VERSION = 0;
    private static final Integer UPDATED_VERSION = 1;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP);

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    @Inject
    private WeightRepository weightRepository;

    @Inject
    private WeightSearchRepository weightSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeightMockMvc;

    private Weight weight;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceService preferenceService;
    
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeightResource weightResource = new WeightResource();
        ReflectionTestUtils.setField(weightResource, "weightSearchRepository", weightSearchRepository);
        ReflectionTestUtils.setField(weightResource, "weightRepository", weightRepository);
        ReflectionTestUtils.setField(weightResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(weightResource, "preferenceService", preferenceService);
        this.restWeightMockMvc = MockMvcBuilders.standaloneSetup(weightResource)
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
        weight = new Weight();
//        weight.setVersion(DEFAULT_VERSION);
        weight.setTimestamp(DEFAULT_TIMESTAMP);
        weight.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createWeight() throws Exception {

        this.createUsuarioConRolUser();

        int databaseSizeBeforeCreate = weightRepository.findAll().size();

        // Create the Weight

        restWeightMockMvc.perform(post("/api/weights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weight)))
                .andExpect(status().isCreated());

        // Validate the Weight in the database
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeCreate + 1);
        Weight testWeight = weights.get(weights.size() - 1);
        assertThat(testWeight.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testWeight.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testWeight.getWeight()).isEqualTo(DEFAULT_WEIGHT);

        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = weightRepository.findAll().size();
        // set the field null
        weight.setTimestamp(null);

        // Create the Weight, which fails.

        restWeightMockMvc.perform(post("/api/weights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weight)))
                .andExpect(status().isBadRequest());

        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = weightRepository.findAll().size();
        // set the field null
        weight.setWeight(null);

        // Create the Weight, which fails.

        restWeightMockMvc.perform(post("/api/weights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weight)))
                .andExpect(status().isBadRequest());

        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeights() throws Exception {
    	
    	weight.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get all the weights
        restWeightMockMvc.perform(get("/api/weights?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
        
        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void getWeight() throws Exception {
    	
    	weight.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(weight.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
        
        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void getNonExistingWeight() throws Exception {
        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeight() throws Exception {
    	
    	weight.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        weightRepository.saveAndFlush(weight);

		int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight
//        weight.setVersion(UPDATED_VERSION);
        weight.setTimestamp(UPDATED_TIMESTAMP);
        weight.setWeight(UPDATED_WEIGHT);

        restWeightMockMvc.perform(put("/api/weights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weight)))
                .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weights.get(weights.size() - 1);
        assertThat(testWeight.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testWeight.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testWeight.getWeight()).isEqualTo(UPDATED_WEIGHT);
        
        this.deleteUsuarioConRolUser();
    }

    @Test
    @Transactional
    public void deleteWeight() throws Exception {
    	
    	weight.setUser(createUsuarioConRolUser());
    	
        // Initialize the database
        weightRepository.saveAndFlush(weight);

		int databaseSizeBeforeDelete = weightRepository.findAll().size();

        // Get the weight
        restWeightMockMvc.perform(delete("/api/weights/{id}", weight.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeDelete - 1);
        
        this.deleteUsuarioConRolUser();
    }
    
    private void createWeightBForLast30Days() {
		User user = this.createUsuarioConRolUser();
		weightRepository.deleteAll();

    	ZonedDateTime now = ZonedDateTime.now().withNano(0);
    	weight = new Weight(now, 80.0, user);
    	weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(10), 80.10, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(20), 80.20, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(27), 80.27, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(28), 80.28, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(29), 80.29, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(30), 80.30, user);
		weightRepository.saveAndFlush(weight);
		weight = new Weight(now.minusDays(31), 80.31, user);
		weightRepository.saveAndFlush(weight);
    }
    
    @Test
    @Transactional
    public void getWeightForLast30Days() throws Exception {
    	createWeightBForLast30Days();
        
        // Get all the blood pressure readings
        restWeightMockMvc.perform(get("/api/weights"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(8)));
        
        // Get the blood pressure readings for the last 30 days
        restWeightMockMvc.perform(get("/api/w-by-days"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.period").value(30))
        	.andExpect(jsonPath("$.readings.[*].weight").value(hasItems(80.0, 80.10, 80.20, 80.27, 80.28, 80.29)))
        	.andExpect(jsonPath("$.readings.[*].weight").value(hasItem(80.30)));
//        	.andExpect(jsonPath("$.readings.[*].weight").value(hasItem(80.31)));	// ERROR minusDays(31)

        this.deleteUsuarioConRolUser();
    }

    // Crea datos en meses distintos
    private void createWeightByMonth(String fecha) {
		User user = this.createUsuarioConRolUser();
		weightRepository.deleteAll();

		LocalDate fechaDate = LocalDate.parse(fecha);
		ZonedDateTime primerDia = fechaDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneOffset.UTC);

    	weight = new Weight(primerDia.minusDays(1), 80.99, user);	// 2014-05-31
    	weightRepository.saveAndFlush(weight);
    	weight = new Weight(primerDia, 80.01, user);				// 2014-06-01
    	weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(3), 80.04, user);	// 2014-06-04
		weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(23), 80.24, user);	// 2014-06-24
		weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(28), 80.29, user);	// 2014-06-29
		weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(29), 80.30, user);	// 2014-06-30
		weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(30), 80.31, user);	// 2014-07-01
		weightRepository.saveAndFlush(weight);
		weight = new Weight(primerDia.plusDays(31), 80.32, user);	// 2014-07-02
		weightRepository.saveAndFlush(weight);
    }
    
    @Test
    @Transactional
    public void getWByMonth() throws Exception {
    	String fecha = "2014-06-15";
        createWeightByMonth(fecha);
        
        // Get all the blood pressure readings
        restWeightMockMvc.perform(get("/api/weights"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(8)));
        
        // Get the blood pressure readings for the last 30 days
        restWeightMockMvc.perform(get("/api/w-by-month/{month}", fecha))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        	.andExpect(jsonPath("$.[*].weight").value(hasItems(80.01, 80.04, 80.24, 80.29)))
        	.andExpect(jsonPath("$.[*].weight").value(hasItem(80.30)));
//    		.andExpect(jsonPath("$.[*].weight").value(hasItem(80.31)));	// ERROR
//        	.andExpect(jsonPath("$.[*].weight").value(hasItem(80.99)));	// ERROR

        this.deleteUsuarioConRolUser();
    }

}
