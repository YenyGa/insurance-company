package com.insurance.web.rest;

import com.insurance.InsuranceApp;
import com.insurance.domain.Insurance;
import com.insurance.domain.User;
import com.insurance.domain.enumeration.InsuranceType;
import com.insurance.domain.enumeration.RiskType;
import com.insurance.repository.InsuranceRepository;
import com.insurance.repository.UserRepository;
import com.insurance.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.insurance.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Integration tests for the {@link InsuranceResource} REST controller.
 */
@SpringBootTest(classes = InsuranceApp.class)
public class InsuranceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_COVERAGE_PERCENTAGE = 1;
    private static final Integer UPDATED_COVERAGE_PERCENTAGE = 2;
    private static final Integer SMALLER_COVERAGE_PERCENTAGE = 1 - 1;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_START_DATE = Instant.ofEpochMilli(-1L);

    private static final Integer DEFAULT_COVERAGE_PERIOD = 1;
    private static final Integer UPDATED_COVERAGE_PERIOD = 2;
    private static final Long SMALLER_COVERAGE_PERIOD = 1L - 1L;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final InsuranceType DEFAULT_INSURANCE_TYPE = InsuranceType.EARTHQUAKE;
    private static final InsuranceType UPDATED_INSURANCE_TYPE = InsuranceType.FIRE;

    private static final RiskType DEFAULT_RISK_TYPE = RiskType.LOW;
    private static final RiskType UPDATED_RISK_TYPE = RiskType.MID;

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String DEFAULT_LASTNAME = "doe";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String DEFAULT_LANGKEY = "en";

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInsuranceMockMvc;

    private Insurance insurance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InsuranceResource insuranceResource = new InsuranceResource(insuranceRepository);
        this.restInsuranceMockMvc = MockMvcBuilders.standaloneSetup(insuranceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurance createEntity(EntityManager em) {
        Insurance insurance = new Insurance()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .coveragePercentage(DEFAULT_COVERAGE_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .coveragePeriod(DEFAULT_COVERAGE_PERIOD)
            .price(DEFAULT_PRICE)
            .insuranceType(DEFAULT_INSURANCE_TYPE)
            .riskType(DEFAULT_RISK_TYPE);
        return insurance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurance createUpdatedEntity(EntityManager em) {
        Insurance insurance = new Insurance()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coveragePercentage(UPDATED_COVERAGE_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .coveragePeriod(UPDATED_COVERAGE_PERIOD)
            .price(UPDATED_PRICE)
            .insuranceType(UPDATED_INSURANCE_TYPE)
            .riskType(UPDATED_RISK_TYPE);
        return insurance;
    }

    @BeforeEach
    public void initTest() {
        insurance = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsurance() throws Exception {
        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();

        // Create the Insurance
        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isCreated());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate + 1);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsurance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInsurance.getCoveragePercentage()).isEqualTo(DEFAULT_COVERAGE_PERCENTAGE);
        assertThat(testInsurance.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testInsurance.getCoveragePeriod()).isEqualTo(DEFAULT_COVERAGE_PERIOD);
        assertThat(testInsurance.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testInsurance.getInsuranceType()).isEqualTo(DEFAULT_INSURANCE_TYPE);
        assertThat(testInsurance.getRiskType()).isEqualTo(DEFAULT_RISK_TYPE);
    }

    @Test
    @Transactional
    public void createInsuranceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();

        // Create the Insurance with an existing ID
        insurance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCoveragePercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setCoveragePercentage(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setStartDate(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoveragePeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setCoveragePeriod(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setPrice(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInsuranceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setInsuranceType(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRiskTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRepository.findAll().size();
        // set the field null
        insurance.setRiskType(null);

        // Create the Insurance, which fails.

        restInsuranceMockMvc.perform(post("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsurances() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList
        restInsuranceMockMvc.perform(get("/api/insurances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].coveragePercentage").value(hasItem(DEFAULT_COVERAGE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].coveragePeriod").value(hasItem(DEFAULT_COVERAGE_PERIOD.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].insuranceType").value(hasItem(DEFAULT_INSURANCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getAllInsurancesByUser() throws Exception {
        // Initialize the database
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);

        User savedUser = userRepository.saveAndFlush(user);

        Insurance insurance = new Insurance()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .coveragePercentage(DEFAULT_COVERAGE_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .coveragePeriod(DEFAULT_COVERAGE_PERIOD)
            .price(DEFAULT_PRICE)
            .insuranceType(DEFAULT_INSURANCE_TYPE)
            .riskType(DEFAULT_RISK_TYPE)
            .user(savedUser);

        Insurance insurance2 = new Insurance()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .coveragePercentage(DEFAULT_COVERAGE_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .coveragePeriod(DEFAULT_COVERAGE_PERIOD)
            .price(DEFAULT_PRICE)
            .insuranceType(DEFAULT_INSURANCE_TYPE)
            .riskType(DEFAULT_RISK_TYPE)
            .user(savedUser);

        insuranceRepository.saveAndFlush(insurance);
        insuranceRepository.saveAndFlush(insurance2);

        // Get the insuranceList by User
        restInsuranceMockMvc.perform(get("/api/insurances?userId=" +savedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].user.id").value(hasItem(savedUser.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get the insurance
        restInsuranceMockMvc.perform(get("/api/insurances/{id}", insurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insurance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.coveragePercentage").value(DEFAULT_COVERAGE_PERCENTAGE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.coveragePeriod").value(DEFAULT_COVERAGE_PERIOD.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.insuranceType").value(DEFAULT_INSURANCE_TYPE.toString()))
            .andExpect(jsonPath("$.riskType").value(DEFAULT_RISK_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsurance() throws Exception {
        // Get the insurance
        restInsuranceMockMvc.perform(get("/api/insurances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Update the insurance
        Insurance updatedInsurance = insuranceRepository.findById(insurance.getId()).get();
        // Disconnect from session so that the updates on updatedInsurance are not directly saved in db
        em.detach(updatedInsurance);
        updatedInsurance
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coveragePercentage(UPDATED_COVERAGE_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .coveragePeriod(UPDATED_COVERAGE_PERIOD)
            .price(UPDATED_PRICE)
            .insuranceType(UPDATED_INSURANCE_TYPE)
            .riskType(UPDATED_RISK_TYPE);

        restInsuranceMockMvc.perform(put("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInsurance)))
            .andExpect(status().isOk());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsurance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInsurance.getCoveragePercentage()).isEqualTo(UPDATED_COVERAGE_PERCENTAGE);
        assertThat(testInsurance.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testInsurance.getCoveragePeriod()).isEqualTo(UPDATED_COVERAGE_PERIOD);
        assertThat(testInsurance.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInsurance.getInsuranceType()).isEqualTo(UPDATED_INSURANCE_TYPE);
        assertThat(testInsurance.getRiskType()).isEqualTo(UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Create the Insurance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceMockMvc.perform(put("/api/insurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insurance)))
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeDelete = insuranceRepository.findAll().size();

        // Delete the insurance
        restInsuranceMockMvc.perform(delete("/api/insurances/{id}", insurance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insurance.class);
        Insurance insurance1 = new Insurance();
        insurance1.setId(1L);
        Insurance insurance2 = new Insurance();
        insurance2.setId(insurance1.getId());
        assertThat(insurance1).isEqualTo(insurance2);
        insurance2.setId(2L);
        assertThat(insurance1).isNotEqualTo(insurance2);
        insurance1.setId(null);
        assertThat(insurance1).isNotEqualTo(insurance2);
    }
}
