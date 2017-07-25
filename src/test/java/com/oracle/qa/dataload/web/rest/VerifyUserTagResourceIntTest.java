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
import com.oracle.qa.dataload.domain.VerifyUserTag;
import com.oracle.qa.dataload.repository.VerifyUserTagRepository;
import com.oracle.qa.dataload.service.VerifyUserTagService;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;
import com.oracle.qa.dataload.service.mapper.VerifyUserTagMapper;
import com.oracle.qa.dataload.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the VerifyUserTagResource REST controller.
 *
 * @see VerifyUserTagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLoadApp.class)
public class VerifyUserTagResourceIntTest {

    private static final byte[] DEFAULT_INPUT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INPUT_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_INPUT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INPUT_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CATEGORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_START_FROM = 1;
    private static final Integer UPDATED_START_FROM = 2;

    private static final Integer DEFAULT_TO_LINE = 1;
    private static final Integer UPDATED_TO_LINE = 2;

    private static final byte[] DEFAULT_OUTPUT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_OUTPUT_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_OUTPUT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_OUTPUT_FILE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_VERIFY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VERIFY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private VerifyUserTagRepository verifyUserTagRepository;

    @Autowired
    private VerifyUserTagMapper verifyUserTagMapper;

    @Autowired
    private VerifyUserTagService verifyUserTagService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVerifyUserTagMockMvc;

    private VerifyUserTag verifyUserTag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VerifyUserTagResource verifyUserTagResource = new VerifyUserTagResource(verifyUserTagService);
        this.restVerifyUserTagMockMvc = MockMvcBuilders.standaloneSetup(verifyUserTagResource)
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
    public static VerifyUserTag createEntity(EntityManager em) {
        VerifyUserTag verifyUserTag = new VerifyUserTag()
            .inputFile(DEFAULT_INPUT_FILE)
            .inputFileContentType(DEFAULT_INPUT_FILE_CONTENT_TYPE)
            .categoryId(DEFAULT_CATEGORY_ID)
            .startFrom(DEFAULT_START_FROM)
            .toLine(DEFAULT_TO_LINE)
            .outputFile(DEFAULT_OUTPUT_FILE)
            .outputFileContentType(DEFAULT_OUTPUT_FILE_CONTENT_TYPE)
            .verifyDate(DEFAULT_VERIFY_DATE);
        return verifyUserTag;
    }

    @Before
    public void initTest() {
        verifyUserTag = createEntity(em);
    }

    @Test
    @Transactional
    public void createVerifyUserTag() throws Exception {
        int databaseSizeBeforeCreate = verifyUserTagRepository.findAll().size();

        // Create the VerifyUserTag
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagMapper.toDto(verifyUserTag);
        restVerifyUserTagMockMvc.perform(post("/api/verify-user-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verifyUserTagDTO)))
            .andExpect(status().isCreated());

        // Validate the VerifyUserTag in the database
        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeCreate + 1);
        VerifyUserTag testVerifyUserTag = verifyUserTagList.get(verifyUserTagList.size() - 1);
        assertThat(testVerifyUserTag.getInputFile()).isEqualTo(DEFAULT_INPUT_FILE);
        assertThat(testVerifyUserTag.getInputFileContentType()).isEqualTo(DEFAULT_INPUT_FILE_CONTENT_TYPE);
        assertThat(testVerifyUserTag.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testVerifyUserTag.getStartFrom()).isEqualTo(DEFAULT_START_FROM);
        assertThat(testVerifyUserTag.getToLine()).isEqualTo(DEFAULT_TO_LINE);
        assertThat(testVerifyUserTag.getOutputFile()).isEqualTo(DEFAULT_OUTPUT_FILE);
        assertThat(testVerifyUserTag.getOutputFileContentType()).isEqualTo(DEFAULT_OUTPUT_FILE_CONTENT_TYPE);
        assertThat(testVerifyUserTag.getVerifyDate()).isEqualTo(DEFAULT_VERIFY_DATE);
    }

    @Test
    @Transactional
    public void createVerifyUserTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = verifyUserTagRepository.findAll().size();

        // Create the VerifyUserTag with an existing ID
        verifyUserTag.setId(1L);
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagMapper.toDto(verifyUserTag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerifyUserTagMockMvc.perform(post("/api/verify-user-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verifyUserTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInputFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = verifyUserTagRepository.findAll().size();
        // set the field null
        verifyUserTag.setInputFile(null);

        // Create the VerifyUserTag, which fails.
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagMapper.toDto(verifyUserTag);

        restVerifyUserTagMockMvc.perform(post("/api/verify-user-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verifyUserTagDTO)))
            .andExpect(status().isBadRequest());

        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVerifyUserTags() throws Exception {
        // Initialize the database
        verifyUserTagRepository.saveAndFlush(verifyUserTag);

        // Get all the verifyUserTagList
        restVerifyUserTagMockMvc.perform(get("/api/verify-user-tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verifyUserTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].inputFileContentType").value(hasItem(DEFAULT_INPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].inputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_INPUT_FILE))))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.toString())))
            .andExpect(jsonPath("$.[*].startFrom").value(hasItem(DEFAULT_START_FROM)))
            .andExpect(jsonPath("$.[*].toLine").value(hasItem(DEFAULT_TO_LINE)))
            .andExpect(jsonPath("$.[*].outputFileContentType").value(hasItem(DEFAULT_OUTPUT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].outputFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_OUTPUT_FILE))))
            .andExpect(jsonPath("$.[*].verifyDate").value(hasItem(DEFAULT_VERIFY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVerifyUserTag() throws Exception {
        // Initialize the database
        verifyUserTagRepository.saveAndFlush(verifyUserTag);

        // Get the verifyUserTag
        restVerifyUserTagMockMvc.perform(get("/api/verify-user-tags/{id}", verifyUserTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(verifyUserTag.getId().intValue()))
            .andExpect(jsonPath("$.inputFileContentType").value(DEFAULT_INPUT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.inputFile").value(Base64Utils.encodeToString(DEFAULT_INPUT_FILE)))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.startFrom").value(DEFAULT_START_FROM))
            .andExpect(jsonPath("$.toLine").value(DEFAULT_TO_LINE))
            .andExpect(jsonPath("$.outputFileContentType").value(DEFAULT_OUTPUT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.outputFile").value(Base64Utils.encodeToString(DEFAULT_OUTPUT_FILE)))
            .andExpect(jsonPath("$.verifyDate").value(DEFAULT_VERIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVerifyUserTag() throws Exception {
        // Get the verifyUserTag
        restVerifyUserTagMockMvc.perform(get("/api/verify-user-tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerifyUserTag() throws Exception {
        // Initialize the database
        verifyUserTagRepository.saveAndFlush(verifyUserTag);
        int databaseSizeBeforeUpdate = verifyUserTagRepository.findAll().size();

        // Update the verifyUserTag
        VerifyUserTag updatedVerifyUserTag = verifyUserTagRepository.findOne(verifyUserTag.getId());
        updatedVerifyUserTag
            .inputFile(UPDATED_INPUT_FILE)
            .inputFileContentType(UPDATED_INPUT_FILE_CONTENT_TYPE)
            .categoryId(UPDATED_CATEGORY_ID)
            .startFrom(UPDATED_START_FROM)
            .toLine(UPDATED_TO_LINE)
            .outputFile(UPDATED_OUTPUT_FILE)
            .outputFileContentType(UPDATED_OUTPUT_FILE_CONTENT_TYPE)
            .verifyDate(UPDATED_VERIFY_DATE);
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagMapper.toDto(updatedVerifyUserTag);

        restVerifyUserTagMockMvc.perform(put("/api/verify-user-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verifyUserTagDTO)))
            .andExpect(status().isOk());

        // Validate the VerifyUserTag in the database
        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeUpdate);
        VerifyUserTag testVerifyUserTag = verifyUserTagList.get(verifyUserTagList.size() - 1);
        assertThat(testVerifyUserTag.getInputFile()).isEqualTo(UPDATED_INPUT_FILE);
        assertThat(testVerifyUserTag.getInputFileContentType()).isEqualTo(UPDATED_INPUT_FILE_CONTENT_TYPE);
        assertThat(testVerifyUserTag.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testVerifyUserTag.getStartFrom()).isEqualTo(UPDATED_START_FROM);
        assertThat(testVerifyUserTag.getToLine()).isEqualTo(UPDATED_TO_LINE);
        assertThat(testVerifyUserTag.getOutputFile()).isEqualTo(UPDATED_OUTPUT_FILE);
        assertThat(testVerifyUserTag.getOutputFileContentType()).isEqualTo(UPDATED_OUTPUT_FILE_CONTENT_TYPE);
        assertThat(testVerifyUserTag.getVerifyDate()).isEqualTo(UPDATED_VERIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVerifyUserTag() throws Exception {
        int databaseSizeBeforeUpdate = verifyUserTagRepository.findAll().size();

        // Create the VerifyUserTag
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagMapper.toDto(verifyUserTag);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVerifyUserTagMockMvc.perform(put("/api/verify-user-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verifyUserTagDTO)))
            .andExpect(status().isCreated());

        // Validate the VerifyUserTag in the database
        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVerifyUserTag() throws Exception {
        // Initialize the database
        verifyUserTagRepository.saveAndFlush(verifyUserTag);
        int databaseSizeBeforeDelete = verifyUserTagRepository.findAll().size();

        // Get the verifyUserTag
        restVerifyUserTagMockMvc.perform(delete("/api/verify-user-tags/{id}", verifyUserTag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VerifyUserTag> verifyUserTagList = verifyUserTagRepository.findAll();
        assertThat(verifyUserTagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerifyUserTag.class);
        VerifyUserTag verifyUserTag1 = new VerifyUserTag();
        verifyUserTag1.setId(1L);
        VerifyUserTag verifyUserTag2 = new VerifyUserTag();
        verifyUserTag2.setId(verifyUserTag1.getId());
        assertThat(verifyUserTag1).isEqualTo(verifyUserTag2);
        verifyUserTag2.setId(2L);
        assertThat(verifyUserTag1).isNotEqualTo(verifyUserTag2);
        verifyUserTag1.setId(null);
        assertThat(verifyUserTag1).isNotEqualTo(verifyUserTag2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerifyUserTagDTO.class);
        VerifyUserTagDTO verifyUserTagDTO1 = new VerifyUserTagDTO();
        verifyUserTagDTO1.setId(1L);
        VerifyUserTagDTO verifyUserTagDTO2 = new VerifyUserTagDTO();
        assertThat(verifyUserTagDTO1).isNotEqualTo(verifyUserTagDTO2);
        verifyUserTagDTO2.setId(verifyUserTagDTO1.getId());
        assertThat(verifyUserTagDTO1).isEqualTo(verifyUserTagDTO2);
        verifyUserTagDTO2.setId(2L);
        assertThat(verifyUserTagDTO1).isNotEqualTo(verifyUserTagDTO2);
        verifyUserTagDTO1.setId(null);
        assertThat(verifyUserTagDTO1).isNotEqualTo(verifyUserTagDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(verifyUserTagMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(verifyUserTagMapper.fromId(null)).isNull();
    }
}
