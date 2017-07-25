package com.oracle.qa.dataload.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.oracle.qa.dataload.DataLoadApp;
import com.oracle.qa.dataload.domain.ReTagProfile;
import com.oracle.qa.dataload.repository.ReTagProfileRepository;
import com.oracle.qa.dataload.service.ReTagProfileService;
import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;
import com.oracle.qa.dataload.service.mapper.ReTagProfileMapper;
import com.oracle.qa.dataload.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ReTagProfileResource REST controller.
 *
 * @see ReTagProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLoadApp.class)
public class ReTagProfileResourceIntTest {

    private static final Integer DEFAULT_SITE_ID = 1;
    private static final Integer UPDATED_SITE_ID = 2;

    private static final byte[] DEFAULT_INPUT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INPUT_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_INPUT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INPUT_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PHINT = "AAAAAAAAAA";
    private static final String UPDATED_PHINT = "BBBBBBBBBB";

    private static final String DEFAULT_HEADERS = "AAAAAAAAAA";
    private static final String UPDATED_HEADERS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_START_FROM_LINE = 1;
    private static final Integer UPDATED_START_FROM_LINE = 2;

    private static final Integer DEFAULT_TO_LINE = 1;
    private static final Integer UPDATED_TO_LINE = 2;

    @Autowired
    private ReTagProfileRepository reTagProfileRepository;

    @Autowired
    private ReTagProfileMapper reTagProfileMapper;

    @Autowired
    private ReTagProfileService reTagProfileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReTagProfileMockMvc;

    private ReTagProfile reTagProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReTagProfileResource reTagProfileResource = new ReTagProfileResource(reTagProfileService);
        this.restReTagProfileMockMvc = MockMvcBuilders.standaloneSetup(reTagProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReTagProfile createEntity(EntityManager em) {
        ReTagProfile reTagProfile = new ReTagProfile()
            .siteId(DEFAULT_SITE_ID)
            .inputFile(DEFAULT_INPUT_FILE)
            .inputFileContentType(DEFAULT_INPUT_FILE_CONTENT_TYPE)
            .phint(DEFAULT_PHINT)
            .headers(DEFAULT_HEADERS)
            .createDate(DEFAULT_CREATE_DATE)
            .startFromLine(DEFAULT_START_FROM_LINE)
            .toLine(DEFAULT_TO_LINE);
        return reTagProfile;
    }

    @Before
    public void initTest() {
        reTagProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createReTagProfile() throws Exception {
        int databaseSizeBeforeCreate = reTagProfileRepository.findAll().size();

        // Create the ReTagProfile
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(reTagProfile);
        restReTagProfileMockMvc.perform(post("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the ReTagProfile in the database
        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ReTagProfile testReTagProfile = reTagProfileList.get(reTagProfileList.size() - 1);
        assertThat(testReTagProfile.getSiteId()).isEqualTo(DEFAULT_SITE_ID);
        assertThat(testReTagProfile.getInputFile()).isEqualTo(DEFAULT_INPUT_FILE);
        assertThat(testReTagProfile.getInputFileContentType()).isEqualTo(DEFAULT_INPUT_FILE_CONTENT_TYPE);
        assertThat(testReTagProfile.getPhint()).isEqualTo(DEFAULT_PHINT);
        assertThat(testReTagProfile.getHeaders()).isEqualTo(DEFAULT_HEADERS);
        assertThat(testReTagProfile.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testReTagProfile.getStartFromLine()).isEqualTo(DEFAULT_START_FROM_LINE);
        assertThat(testReTagProfile.getToLine()).isEqualTo(DEFAULT_TO_LINE);
    }

    @Test
    @Transactional
    public void createReTagProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reTagProfileRepository.findAll().size();

        // Create the ReTagProfile with an existing ID
        reTagProfile.setId(1L);
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(reTagProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReTagProfileMockMvc.perform(post("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSiteIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = reTagProfileRepository.findAll().size();
        // set the field null
        reTagProfile.setSiteId(null);

        // Create the ReTagProfile, which fails.
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(reTagProfile);

        restReTagProfileMockMvc.perform(post("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isBadRequest());

        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInputFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = reTagProfileRepository.findAll().size();
        // set the field null
        reTagProfile.setInputFile(null);

        // Create the ReTagProfile, which fails.
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(reTagProfile);

        restReTagProfileMockMvc.perform(post("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isBadRequest());

        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReTagProfiles() throws Exception {
        // Initialize the database
        reTagProfileRepository.saveAndFlush(reTagProfile);

        // Get all the reTagProfileList
        restReTagProfileMockMvc.perform(get("/api/re-tag-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reTagProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteId").value(hasItem(DEFAULT_SITE_ID)))
            .andExpect(jsonPath("$.[*].inputFileContentType").value(hasItem(DEFAULT_INPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].inputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_INPUT_FILE))))
            .andExpect(jsonPath("$.[*].phint").value(hasItem(DEFAULT_PHINT.toString())))
            .andExpect(jsonPath("$.[*].headers").value(hasItem(DEFAULT_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].startFromLine").value(hasItem(DEFAULT_START_FROM_LINE)))
            .andExpect(jsonPath("$.[*].toLine").value(hasItem(DEFAULT_TO_LINE)));
    }

    @Test
    @Transactional
    public void getReTagProfile() throws Exception {
        // Initialize the database
        reTagProfileRepository.saveAndFlush(reTagProfile);

        // Get the reTagProfile
        restReTagProfileMockMvc.perform(get("/api/re-tag-profiles/{id}", reTagProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reTagProfile.getId().intValue()))
            .andExpect(jsonPath("$.siteId").value(DEFAULT_SITE_ID))
            .andExpect(jsonPath("$.inputFileContentType").value(DEFAULT_INPUT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.inputFile").value(Base64Utils.encodeToString(DEFAULT_INPUT_FILE)))
            .andExpect(jsonPath("$.phint").value(DEFAULT_PHINT.toString()))
            .andExpect(jsonPath("$.headers").value(DEFAULT_HEADERS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.startFromLine").value(DEFAULT_START_FROM_LINE))
            .andExpect(jsonPath("$.toLine").value(DEFAULT_TO_LINE));
    }

    @Test
    @Transactional
    public void getNonExistingReTagProfile() throws Exception {
        // Get the reTagProfile
        restReTagProfileMockMvc.perform(get("/api/re-tag-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReTagProfile() throws Exception {
        // Initialize the database
        reTagProfileRepository.saveAndFlush(reTagProfile);
        int databaseSizeBeforeUpdate = reTagProfileRepository.findAll().size();

        // Update the reTagProfile
        ReTagProfile updatedReTagProfile = reTagProfileRepository.findOne(reTagProfile.getId());
        updatedReTagProfile
            .siteId(UPDATED_SITE_ID)
            .inputFile(UPDATED_INPUT_FILE)
            .inputFileContentType(UPDATED_INPUT_FILE_CONTENT_TYPE)
            .phint(UPDATED_PHINT)
            .headers(UPDATED_HEADERS)
            .createDate(UPDATED_CREATE_DATE)
            .startFromLine(UPDATED_START_FROM_LINE)
            .toLine(UPDATED_TO_LINE);
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(updatedReTagProfile);

        restReTagProfileMockMvc.perform(put("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isOk());

        // Validate the ReTagProfile in the database
        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeUpdate);
        ReTagProfile testReTagProfile = reTagProfileList.get(reTagProfileList.size() - 1);
        assertThat(testReTagProfile.getSiteId()).isEqualTo(UPDATED_SITE_ID);
        assertThat(testReTagProfile.getInputFile()).isEqualTo(UPDATED_INPUT_FILE);
        assertThat(testReTagProfile.getInputFileContentType()).isEqualTo(UPDATED_INPUT_FILE_CONTENT_TYPE);
        assertThat(testReTagProfile.getPhint()).isEqualTo(UPDATED_PHINT);
        assertThat(testReTagProfile.getHeaders()).isEqualTo(UPDATED_HEADERS);
        assertThat(testReTagProfile.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testReTagProfile.getStartFromLine()).isEqualTo(UPDATED_START_FROM_LINE);
        assertThat(testReTagProfile.getToLine()).isEqualTo(UPDATED_TO_LINE);
    }

    @Test
    @Transactional
    public void updateNonExistingReTagProfile() throws Exception {
        int databaseSizeBeforeUpdate = reTagProfileRepository.findAll().size();

        // Create the ReTagProfile
        ReTagProfileDTO reTagProfileDTO = reTagProfileMapper.toDto(reTagProfile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReTagProfileMockMvc.perform(put("/api/re-tag-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reTagProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the ReTagProfile in the database
        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReTagProfile() throws Exception {
        // Initialize the database
        reTagProfileRepository.saveAndFlush(reTagProfile);
        int databaseSizeBeforeDelete = reTagProfileRepository.findAll().size();

        // Get the reTagProfile
        restReTagProfileMockMvc.perform(delete("/api/re-tag-profiles/{id}", reTagProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReTagProfile> reTagProfileList = reTagProfileRepository.findAll();
        assertThat(reTagProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReTagProfile.class);
        ReTagProfile reTagProfile1 = new ReTagProfile();
        reTagProfile1.setId(1L);
        ReTagProfile reTagProfile2 = new ReTagProfile();
        reTagProfile2.setId(reTagProfile1.getId());
        assertThat(reTagProfile1).isEqualTo(reTagProfile2);
        reTagProfile2.setId(2L);
        assertThat(reTagProfile1).isNotEqualTo(reTagProfile2);
        reTagProfile1.setId(null);
        assertThat(reTagProfile1).isNotEqualTo(reTagProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReTagProfileDTO.class);
        ReTagProfileDTO reTagProfileDTO1 = new ReTagProfileDTO();
        reTagProfileDTO1.setId(1L);
        ReTagProfileDTO reTagProfileDTO2 = new ReTagProfileDTO();
        assertThat(reTagProfileDTO1).isNotEqualTo(reTagProfileDTO2);
        reTagProfileDTO2.setId(reTagProfileDTO1.getId());
        assertThat(reTagProfileDTO1).isEqualTo(reTagProfileDTO2);
        reTagProfileDTO2.setId(2L);
        assertThat(reTagProfileDTO1).isNotEqualTo(reTagProfileDTO2);
        reTagProfileDTO1.setId(null);
        assertThat(reTagProfileDTO1).isNotEqualTo(reTagProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reTagProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reTagProfileMapper.fromId(null)).isNull();
    }
}
