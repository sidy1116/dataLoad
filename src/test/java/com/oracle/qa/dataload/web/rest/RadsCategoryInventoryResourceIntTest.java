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

import com.oracle.qa.dataload.DataLoadApp;
import com.oracle.qa.dataload.domain.RadsCategoryInventory;
import com.oracle.qa.dataload.repository.RadsCategoryInventoryRepository;
import com.oracle.qa.dataload.service.RadsCategoryInventoryService;
import com.oracle.qa.dataload.service.dto.RadsCategoryInventoryDTO;
import com.oracle.qa.dataload.service.mapper.RadsCategoryInventoryMapper;
import com.oracle.qa.dataload.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the RadsCategoryInventoryResource REST controller.
 *
 * @see RadsCategoryInventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLoadApp.class)
public class RadsCategoryInventoryResourceIntTest {

    private static final Integer DEFAULT_CAT_ID = 1;
    private static final Integer UPDATED_CAT_ID = 2;

    private static final String DEFAULT_CAT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CAT_NAME = "BBBBBBBBBB";

    @Autowired
    private RadsCategoryInventoryRepository radsCategoryInventoryRepository;

    @Autowired
    private RadsCategoryInventoryMapper radsCategoryInventoryMapper;

    @Autowired
    private RadsCategoryInventoryService radsCategoryInventoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRadsCategoryInventoryMockMvc;

    private RadsCategoryInventory radsCategoryInventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RadsCategoryInventoryResource radsCategoryInventoryResource = new RadsCategoryInventoryResource(radsCategoryInventoryService);
        this.restRadsCategoryInventoryMockMvc = MockMvcBuilders.standaloneSetup(radsCategoryInventoryResource)
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
    public static RadsCategoryInventory createEntity(EntityManager em) {
        RadsCategoryInventory radsCategoryInventory = new RadsCategoryInventory()
            .catId(DEFAULT_CAT_ID)
            .catName(DEFAULT_CAT_NAME);
        return radsCategoryInventory;
    }

    @Before
    public void initTest() {
        radsCategoryInventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRadsCategoryInventory() throws Exception {
        int databaseSizeBeforeCreate = radsCategoryInventoryRepository.findAll().size();

        // Create the RadsCategoryInventory
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(radsCategoryInventory);
        restRadsCategoryInventoryMockMvc.perform(post("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RadsCategoryInventory in the database
        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        RadsCategoryInventory testRadsCategoryInventory = radsCategoryInventoryList.get(radsCategoryInventoryList.size() - 1);
        assertThat(testRadsCategoryInventory.getCatId()).isEqualTo(DEFAULT_CAT_ID);
        assertThat(testRadsCategoryInventory.getCatName()).isEqualTo(DEFAULT_CAT_NAME);
    }

    @Test
    @Transactional
    public void createRadsCategoryInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = radsCategoryInventoryRepository.findAll().size();

        // Create the RadsCategoryInventory with an existing ID
        radsCategoryInventory.setId(1L);
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(radsCategoryInventory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRadsCategoryInventoryMockMvc.perform(post("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCatIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = radsCategoryInventoryRepository.findAll().size();
        // set the field null
        radsCategoryInventory.setCatId(null);

        // Create the RadsCategoryInventory, which fails.
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(radsCategoryInventory);

        restRadsCategoryInventoryMockMvc.perform(post("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCatNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = radsCategoryInventoryRepository.findAll().size();
        // set the field null
        radsCategoryInventory.setCatName(null);

        // Create the RadsCategoryInventory, which fails.
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(radsCategoryInventory);

        restRadsCategoryInventoryMockMvc.perform(post("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRadsCategoryInventories() throws Exception {
        // Initialize the database
        radsCategoryInventoryRepository.saveAndFlush(radsCategoryInventory);

        // Get all the radsCategoryInventoryList
        restRadsCategoryInventoryMockMvc.perform(get("/api/rads-category-inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(radsCategoryInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].catId").value(hasItem(DEFAULT_CAT_ID)))
            .andExpect(jsonPath("$.[*].catName").value(hasItem(DEFAULT_CAT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRadsCategoryInventory() throws Exception {
        // Initialize the database
        radsCategoryInventoryRepository.saveAndFlush(radsCategoryInventory);

        // Get the radsCategoryInventory
        restRadsCategoryInventoryMockMvc.perform(get("/api/rads-category-inventories/{id}", radsCategoryInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(radsCategoryInventory.getId().intValue()))
            .andExpect(jsonPath("$.catId").value(DEFAULT_CAT_ID))
            .andExpect(jsonPath("$.catName").value(DEFAULT_CAT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRadsCategoryInventory() throws Exception {
        // Get the radsCategoryInventory
        restRadsCategoryInventoryMockMvc.perform(get("/api/rads-category-inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRadsCategoryInventory() throws Exception {
        // Initialize the database
        radsCategoryInventoryRepository.saveAndFlush(radsCategoryInventory);
        int databaseSizeBeforeUpdate = radsCategoryInventoryRepository.findAll().size();

        // Update the radsCategoryInventory
        RadsCategoryInventory updatedRadsCategoryInventory = radsCategoryInventoryRepository.findOne(radsCategoryInventory.getId());
        updatedRadsCategoryInventory
            .catId(UPDATED_CAT_ID)
            .catName(UPDATED_CAT_NAME);
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(updatedRadsCategoryInventory);

        restRadsCategoryInventoryMockMvc.perform(put("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isOk());

        // Validate the RadsCategoryInventory in the database
        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeUpdate);
        RadsCategoryInventory testRadsCategoryInventory = radsCategoryInventoryList.get(radsCategoryInventoryList.size() - 1);
        assertThat(testRadsCategoryInventory.getCatId()).isEqualTo(UPDATED_CAT_ID);
        assertThat(testRadsCategoryInventory.getCatName()).isEqualTo(UPDATED_CAT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRadsCategoryInventory() throws Exception {
        int databaseSizeBeforeUpdate = radsCategoryInventoryRepository.findAll().size();

        // Create the RadsCategoryInventory
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryMapper.toDto(radsCategoryInventory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRadsCategoryInventoryMockMvc.perform(put("/api/rads-category-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsCategoryInventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RadsCategoryInventory in the database
        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRadsCategoryInventory() throws Exception {
        // Initialize the database
        radsCategoryInventoryRepository.saveAndFlush(radsCategoryInventory);
        int databaseSizeBeforeDelete = radsCategoryInventoryRepository.findAll().size();

        // Get the radsCategoryInventory
        restRadsCategoryInventoryMockMvc.perform(delete("/api/rads-category-inventories/{id}", radsCategoryInventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RadsCategoryInventory> radsCategoryInventoryList = radsCategoryInventoryRepository.findAll();
        assertThat(radsCategoryInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RadsCategoryInventory.class);
        RadsCategoryInventory radsCategoryInventory1 = new RadsCategoryInventory();
        radsCategoryInventory1.setId(1L);
        RadsCategoryInventory radsCategoryInventory2 = new RadsCategoryInventory();
        radsCategoryInventory2.setId(radsCategoryInventory1.getId());
        assertThat(radsCategoryInventory1).isEqualTo(radsCategoryInventory2);
        radsCategoryInventory2.setId(2L);
        assertThat(radsCategoryInventory1).isNotEqualTo(radsCategoryInventory2);
        radsCategoryInventory1.setId(null);
        assertThat(radsCategoryInventory1).isNotEqualTo(radsCategoryInventory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RadsCategoryInventoryDTO.class);
        RadsCategoryInventoryDTO radsCategoryInventoryDTO1 = new RadsCategoryInventoryDTO();
        radsCategoryInventoryDTO1.setId(1L);
        RadsCategoryInventoryDTO radsCategoryInventoryDTO2 = new RadsCategoryInventoryDTO();
        assertThat(radsCategoryInventoryDTO1).isNotEqualTo(radsCategoryInventoryDTO2);
        radsCategoryInventoryDTO2.setId(radsCategoryInventoryDTO1.getId());
        assertThat(radsCategoryInventoryDTO1).isEqualTo(radsCategoryInventoryDTO2);
        radsCategoryInventoryDTO2.setId(2L);
        assertThat(radsCategoryInventoryDTO1).isNotEqualTo(radsCategoryInventoryDTO2);
        radsCategoryInventoryDTO1.setId(null);
        assertThat(radsCategoryInventoryDTO1).isNotEqualTo(radsCategoryInventoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(radsCategoryInventoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(radsCategoryInventoryMapper.fromId(null)).isNull();
    }
}
