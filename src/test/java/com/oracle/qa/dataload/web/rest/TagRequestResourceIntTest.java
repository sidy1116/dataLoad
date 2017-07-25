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
import com.oracle.qa.dataload.domain.TagRequest;
import com.oracle.qa.dataload.domain.enumeration.IdType;
import com.oracle.qa.dataload.repository.TagRequestRepository;
import com.oracle.qa.dataload.service.TagRequestService;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;
import com.oracle.qa.dataload.service.mapper.TagRequestMapper;
import com.oracle.qa.dataload.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the TagRequestResource REST controller.
 *
 * @see TagRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLoadApp.class)
public class TagRequestResourceIntTest {

    private static final Integer DEFAULT_SITE_ID = 1;
    private static final Integer UPDATED_SITE_ID = 2;

    private static final String DEFAULT_PHINTS = "AAAAAAAAAA";
    private static final String UPDATED_PHINTS = "BBBBBBBBBB";

    private static final String DEFAULT_REFEREL_URL = "AAAAAAAAAA";
    private static final String UPDATED_REFEREL_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HEADERS = "AAAAAAAAAA";
    private static final String UPDATED_HEADERS = "BBBBBBBBBB";

    private static final IdType DEFAULT_ID_TYPE = IdType.bkuuid;
    private static final IdType UPDATED_ID_TYPE = IdType.adid;

    private static final Integer DEFAULT_REQUEST_COUNT = 1;
    private static final Integer UPDATED_REQUEST_COUNT = 2;

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TagRequestRepository tagRequestRepository;

    @Autowired
    private TagRequestMapper tagRequestMapper;

    @Autowired
    private TagRequestService tagRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTagRequestMockMvc;

    private TagRequest tagRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagRequestResource tagRequestResource = new TagRequestResource(tagRequestService);
        this.restTagRequestMockMvc = MockMvcBuilders.standaloneSetup(tagRequestResource)
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
    public static TagRequest createEntity(EntityManager em) {
        TagRequest tagRequest = new TagRequest()
            .siteId(DEFAULT_SITE_ID)
            .phints(DEFAULT_PHINTS)
            .referelUrl(DEFAULT_REFEREL_URL)
            .headers(DEFAULT_HEADERS)
            .idType(DEFAULT_ID_TYPE)
            .requestCount(DEFAULT_REQUEST_COUNT)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .createDate(DEFAULT_CREATE_DATE);
        return tagRequest;
    }

    @Before
    public void initTest() {
        tagRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createTagRequest() throws Exception {
        int databaseSizeBeforeCreate = tagRequestRepository.findAll().size();

        // Create the TagRequest
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);
        restTagRequestMockMvc.perform(post("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the TagRequest in the database
        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeCreate + 1);
        TagRequest testTagRequest = tagRequestList.get(tagRequestList.size() - 1);
        assertThat(testTagRequest.getSiteId()).isEqualTo(DEFAULT_SITE_ID);
        assertThat(testTagRequest.getPhints()).isEqualTo(DEFAULT_PHINTS);
        assertThat(testTagRequest.getReferelUrl()).isEqualTo(DEFAULT_REFEREL_URL);
        assertThat(testTagRequest.getHeaders()).isEqualTo(DEFAULT_HEADERS);
        assertThat(testTagRequest.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
        assertThat(testTagRequest.getRequestCount()).isEqualTo(DEFAULT_REQUEST_COUNT);
        assertThat(testTagRequest.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testTagRequest.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testTagRequest.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createTagRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagRequestRepository.findAll().size();

        // Create the TagRequest with an existing ID
        tagRequest.setId(1L);
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagRequestMockMvc.perform(post("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSiteIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRequestRepository.findAll().size();
        // set the field null
        tagRequest.setSiteId(null);

        // Create the TagRequest, which fails.
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);

        restTagRequestMockMvc.perform(post("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isBadRequest());

        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRequestRepository.findAll().size();
        // set the field null
        tagRequest.setIdType(null);

        // Create the TagRequest, which fails.
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);

        restTagRequestMockMvc.perform(post("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isBadRequest());

        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequestCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRequestRepository.findAll().size();
        // set the field null
        tagRequest.setRequestCount(null);

        // Create the TagRequest, which fails.
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);

        restTagRequestMockMvc.perform(post("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isBadRequest());

        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTagRequests() throws Exception {
        // Initialize the database
        tagRequestRepository.saveAndFlush(tagRequest);

        // Get all the tagRequestList
        restTagRequestMockMvc.perform(get("/api/tag-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteId").value(hasItem(DEFAULT_SITE_ID)))
            .andExpect(jsonPath("$.[*].phints").value(hasItem(DEFAULT_PHINTS.toString())))
            .andExpect(jsonPath("$.[*].referelUrl").value(hasItem(DEFAULT_REFEREL_URL.toString())))
            .andExpect(jsonPath("$.[*].headers").value(hasItem(DEFAULT_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requestCount").value(hasItem(DEFAULT_REQUEST_COUNT)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTagRequest() throws Exception {
        // Initialize the database
        tagRequestRepository.saveAndFlush(tagRequest);

        // Get the tagRequest
        restTagRequestMockMvc.perform(get("/api/tag-requests/{id}", tagRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tagRequest.getId().intValue()))
            .andExpect(jsonPath("$.siteId").value(DEFAULT_SITE_ID))
            .andExpect(jsonPath("$.phints").value(DEFAULT_PHINTS.toString()))
            .andExpect(jsonPath("$.referelUrl").value(DEFAULT_REFEREL_URL.toString()))
            .andExpect(jsonPath("$.headers").value(DEFAULT_HEADERS.toString()))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()))
            .andExpect(jsonPath("$.requestCount").value(DEFAULT_REQUEST_COUNT))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTagRequest() throws Exception {
        // Get the tagRequest
        restTagRequestMockMvc.perform(get("/api/tag-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagRequest() throws Exception {
        // Initialize the database
        tagRequestRepository.saveAndFlush(tagRequest);
        int databaseSizeBeforeUpdate = tagRequestRepository.findAll().size();

        // Update the tagRequest
        TagRequest updatedTagRequest = tagRequestRepository.findOne(tagRequest.getId());
        updatedTagRequest
            .siteId(UPDATED_SITE_ID)
            .phints(UPDATED_PHINTS)
            .referelUrl(UPDATED_REFEREL_URL)
            .headers(UPDATED_HEADERS)
            .idType(UPDATED_ID_TYPE)
            .requestCount(UPDATED_REQUEST_COUNT)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE);
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(updatedTagRequest);

        restTagRequestMockMvc.perform(put("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isOk());

        // Validate the TagRequest in the database
        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeUpdate);
        TagRequest testTagRequest = tagRequestList.get(tagRequestList.size() - 1);
        assertThat(testTagRequest.getSiteId()).isEqualTo(UPDATED_SITE_ID);
        assertThat(testTagRequest.getPhints()).isEqualTo(UPDATED_PHINTS);
        assertThat(testTagRequest.getReferelUrl()).isEqualTo(UPDATED_REFEREL_URL);
        assertThat(testTagRequest.getHeaders()).isEqualTo(UPDATED_HEADERS);
        assertThat(testTagRequest.getIdType()).isEqualTo(UPDATED_ID_TYPE);
        assertThat(testTagRequest.getRequestCount()).isEqualTo(UPDATED_REQUEST_COUNT);
        assertThat(testTagRequest.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testTagRequest.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testTagRequest.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTagRequest() throws Exception {
        int databaseSizeBeforeUpdate = tagRequestRepository.findAll().size();

        // Create the TagRequest
        TagRequestDTO tagRequestDTO = tagRequestMapper.toDto(tagRequest);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTagRequestMockMvc.perform(put("/api/tag-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the TagRequest in the database
        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTagRequest() throws Exception {
        // Initialize the database
        tagRequestRepository.saveAndFlush(tagRequest);
        int databaseSizeBeforeDelete = tagRequestRepository.findAll().size();

        // Get the tagRequest
        restTagRequestMockMvc.perform(delete("/api/tag-requests/{id}", tagRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TagRequest> tagRequestList = tagRequestRepository.findAll();
        assertThat(tagRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagRequest.class);
        TagRequest tagRequest1 = new TagRequest();
        tagRequest1.setId(1L);
        TagRequest tagRequest2 = new TagRequest();
        tagRequest2.setId(tagRequest1.getId());
        assertThat(tagRequest1).isEqualTo(tagRequest2);
        tagRequest2.setId(2L);
        assertThat(tagRequest1).isNotEqualTo(tagRequest2);
        tagRequest1.setId(null);
        assertThat(tagRequest1).isNotEqualTo(tagRequest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagRequestDTO.class);
        TagRequestDTO tagRequestDTO1 = new TagRequestDTO();
        tagRequestDTO1.setId(1L);
        TagRequestDTO tagRequestDTO2 = new TagRequestDTO();
        assertThat(tagRequestDTO1).isNotEqualTo(tagRequestDTO2);
        tagRequestDTO2.setId(tagRequestDTO1.getId());
        assertThat(tagRequestDTO1).isEqualTo(tagRequestDTO2);
        tagRequestDTO2.setId(2L);
        assertThat(tagRequestDTO1).isNotEqualTo(tagRequestDTO2);
        tagRequestDTO1.setId(null);
        assertThat(tagRequestDTO1).isNotEqualTo(tagRequestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tagRequestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tagRequestMapper.fromId(null)).isNull();
    }
}
