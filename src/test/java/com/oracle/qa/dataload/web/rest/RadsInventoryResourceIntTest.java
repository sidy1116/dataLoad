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

import com.oracle.qa.dataload.DataLoadApp;
import com.oracle.qa.dataload.domain.RadsInventory;
import com.oracle.qa.dataload.repository.RadsInventoryRepository;
import com.oracle.qa.dataload.service.RadsInventoryService;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;
import com.oracle.qa.dataload.service.mapper.RadsInventoryMapper;
import com.oracle.qa.dataload.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the RadsInventoryResource REST controller.
 *
 * @see RadsInventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLoadApp.class)
public class RadsInventoryResourceIntTest {

    private static final Integer DEFAULT_CAT_ID = 1;
    private static final Integer UPDATED_CAT_ID = 2;

    private static final String DEFAULT_CAT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CAT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVENTORY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVENTORY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;

    private static final Long DEFAULT_PREV_INV_COUNT = 1L;
    private static final Long UPDATED_PREV_INV_COUNT = 2L;

    private static final String DEFAULT_DIFF_PERCENTAGE = "AAAAAAAAAA";
    private static final String UPDATED_DIFF_PERCENTAGE = "BBBBBBBBBB";

    @Autowired
    private RadsInventoryRepository radsInventoryRepository;

    @Autowired
    private RadsInventoryMapper radsInventoryMapper;

    @Autowired
    private RadsInventoryService radsInventoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRadsInventoryMockMvc;

    private RadsInventory radsInventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RadsInventoryResource radsInventoryResource = new RadsInventoryResource(radsInventoryService);
        this.restRadsInventoryMockMvc = MockMvcBuilders.standaloneSetup(radsInventoryResource)
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
    public static RadsInventory createEntity(EntityManager em) {
        RadsInventory radsInventory = new RadsInventory()
            .catId(DEFAULT_CAT_ID)
            .catName(DEFAULT_CAT_NAME)
            .inventoryDate(DEFAULT_INVENTORY_DATE)
            .count(DEFAULT_COUNT)
            .prevInvCount(DEFAULT_PREV_INV_COUNT)
            .diffPercentage(DEFAULT_DIFF_PERCENTAGE);
        return radsInventory;
    }

    @Before
    public void initTest() {
        radsInventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRadsInventory() throws Exception {
        int databaseSizeBeforeCreate = radsInventoryRepository.findAll().size();

        // Create the RadsInventory
        RadsInventoryDTO radsInventoryDTO = radsInventoryMapper.toDto(radsInventory);
        restRadsInventoryMockMvc.perform(post("/api/rads-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsInventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RadsInventory in the database
        List<RadsInventory> radsInventoryList = radsInventoryRepository.findAll();
        assertThat(radsInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        RadsInventory testRadsInventory = radsInventoryList.get(radsInventoryList.size() - 1);
        assertThat(testRadsInventory.getCatId()).isEqualTo(DEFAULT_CAT_ID);
        assertThat(testRadsInventory.getCatName()).isEqualTo(DEFAULT_CAT_NAME);
        assertThat(testRadsInventory.getInventoryDate()).isEqualTo(DEFAULT_INVENTORY_DATE);
        assertThat(testRadsInventory.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testRadsInventory.getPrevInvCount()).isEqualTo(DEFAULT_PREV_INV_COUNT);
        assertThat(testRadsInventory.getDiffPercentage()).isEqualTo(DEFAULT_DIFF_PERCENTAGE);
    }

    @Test
    @Transactional
    public void createRadsInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = radsInventoryRepository.findAll().size();

        // Create the RadsInventory with an existing ID
        radsInventory.setId(1L);
        RadsInventoryDTO radsInventoryDTO = radsInventoryMapper.toDto(radsInventory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRadsInventoryMockMvc.perform(post("/api/rads-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RadsInventory> radsInventoryList = radsInventoryRepository.findAll();
        assertThat(radsInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRadsInventories() throws Exception {
        // Initialize the database
        radsInventoryRepository.saveAndFlush(radsInventory);

        // Get all the radsInventoryList
        restRadsInventoryMockMvc.perform(get("/api/rads-inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(radsInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].catId").value(hasItem(DEFAULT_CAT_ID)))
            .andExpect(jsonPath("$.[*].catName").value(hasItem(DEFAULT_CAT_NAME.toString())))
            .andExpect(jsonPath("$.[*].inventoryDate").value(hasItem(DEFAULT_INVENTORY_DATE.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].prevInvCount").value(hasItem(DEFAULT_PREV_INV_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].diffPercentage").value(hasItem(DEFAULT_DIFF_PERCENTAGE.toString())));
    }

    @Test
    @Transactional
    public void getRadsInventory() throws Exception {
        // Initialize the database
        radsInventoryRepository.saveAndFlush(radsInventory);

        // Get the radsInventory
        restRadsInventoryMockMvc.perform(get("/api/rads-inventories/{id}", radsInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(radsInventory.getId().intValue()))
            .andExpect(jsonPath("$.catId").value(DEFAULT_CAT_ID))
            .andExpect(jsonPath("$.catName").value(DEFAULT_CAT_NAME.toString()))
            .andExpect(jsonPath("$.inventoryDate").value(DEFAULT_INVENTORY_DATE.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()))
            .andExpect(jsonPath("$.prevInvCount").value(DEFAULT_PREV_INV_COUNT.intValue()))
            .andExpect(jsonPath("$.diffPercentage").value(DEFAULT_DIFF_PERCENTAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRadsInventory() throws Exception {
        // Get the radsInventory
        restRadsInventoryMockMvc.perform(get("/api/rads-inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRadsInventory() throws Exception {
        // Initialize the database
        radsInventoryRepository.saveAndFlush(radsInventory);
        int databaseSizeBeforeUpdate = radsInventoryRepository.findAll().size();

        // Update the radsInventory
        RadsInventory updatedRadsInventory = radsInventoryRepository.findOne(radsInventory.getId());
        updatedRadsInventory
            .catId(UPDATED_CAT_ID)
            .catName(UPDATED_CAT_NAME)
            .inventoryDate(UPDATED_INVENTORY_DATE)
            .count(UPDATED_COUNT)
            .prevInvCount(UPDATED_PREV_INV_COUNT)
            .diffPercentage(UPDATED_DIFF_PERCENTAGE);
        RadsInventoryDTO radsInventoryDTO = radsInventoryMapper.toDto(updatedRadsInventory);

        restRadsInventoryMockMvc.perform(put("/api/rads-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsInventoryDTO)))
            .andExpect(status().isOk());

        // Validate the RadsInventory in the database
        List<RadsInventory> radsInventoryList = radsInventoryRepository.findAll();
        assertThat(radsInventoryList).hasSize(databaseSizeBeforeUpdate);
        RadsInventory testRadsInventory = radsInventoryList.get(radsInventoryList.size() - 1);
        assertThat(testRadsInventory.getCatId()).isEqualTo(UPDATED_CAT_ID);
        assertThat(testRadsInventory.getCatName()).isEqualTo(UPDATED_CAT_NAME);
        assertThat(testRadsInventory.getInventoryDate()).isEqualTo(UPDATED_INVENTORY_DATE);
        assertThat(testRadsInventory.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testRadsInventory.getPrevInvCount()).isEqualTo(UPDATED_PREV_INV_COUNT);
        assertThat(testRadsInventory.getDiffPercentage()).isEqualTo(UPDATED_DIFF_PERCENTAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingRadsInventory() throws Exception {
        int databaseSizeBeforeUpdate = radsInventoryRepository.findAll().size();

        // Create the RadsInventory
        RadsInventoryDTO radsInventoryDTO = radsInventoryMapper.toDto(radsInventory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRadsInventoryMockMvc.perform(put("/api/rads-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(radsInventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RadsInventory in the database
        List<RadsInventory> radsInventoryList = radsInventoryRepository.findAll();
        assertThat(radsInventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRadsInventory() throws Exception {
        // Initialize the database
        radsInventoryRepository.saveAndFlush(radsInventory);
        int databaseSizeBeforeDelete = radsInventoryRepository.findAll().size();

        // Get the radsInventory
        restRadsInventoryMockMvc.perform(delete("/api/rads-inventories/{id}", radsInventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RadsInventory> radsInventoryList = radsInventoryRepository.findAll();
        assertThat(radsInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RadsInventory.class);
        RadsInventory radsInventory1 = new RadsInventory();
        radsInventory1.setId(1L);
        RadsInventory radsInventory2 = new RadsInventory();
        radsInventory2.setId(radsInventory1.getId());
        assertThat(radsInventory1).isEqualTo(radsInventory2);
        radsInventory2.setId(2L);
        assertThat(radsInventory1).isNotEqualTo(radsInventory2);
        radsInventory1.setId(null);
        assertThat(radsInventory1).isNotEqualTo(radsInventory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RadsInventoryDTO.class);
        RadsInventoryDTO radsInventoryDTO1 = new RadsInventoryDTO();
        radsInventoryDTO1.setId(1L);
        RadsInventoryDTO radsInventoryDTO2 = new RadsInventoryDTO();
        assertThat(radsInventoryDTO1).isNotEqualTo(radsInventoryDTO2);
        radsInventoryDTO2.setId(radsInventoryDTO1.getId());
        assertThat(radsInventoryDTO1).isEqualTo(radsInventoryDTO2);
        radsInventoryDTO2.setId(2L);
        assertThat(radsInventoryDTO1).isNotEqualTo(radsInventoryDTO2);
        radsInventoryDTO1.setId(null);
        assertThat(radsInventoryDTO1).isNotEqualTo(radsInventoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(radsInventoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(radsInventoryMapper.fromId(null)).isNull();
    }
}
