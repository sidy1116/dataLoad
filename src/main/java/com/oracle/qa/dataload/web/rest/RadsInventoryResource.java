package com.oracle.qa.dataload.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.oracle.qa.dataload.domain.RadsCategoryInventory;
import com.oracle.qa.dataload.domain.RadsInventory;
import com.oracle.qa.dataload.service.RadsCategoryInventoryService;
import com.oracle.qa.dataload.service.RadsInventoryService;
import com.oracle.qa.dataload.service.async.tasks.RadsInventoryCallTask;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;
import com.oracle.qa.dataload.service.executors.Runner;
import com.oracle.qa.dataload.service.mapper.RadsInventoryMapper;
import com.oracle.qa.dataload.web.rest.util.HeaderUtil;
import com.oracle.qa.dataload.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing RadsInventory.
 */
@RestController
@RequestMapping("/api")
public class RadsInventoryResource {

    private final Logger log = LoggerFactory.getLogger(RadsInventoryResource.class);

    private static final String ENTITY_NAME = "radsInventory";

    private final RadsInventoryService radsInventoryService;
   
    @Autowired
    private  RadsCategoryInventoryService radsCategoryInventoryService;
    
    @Autowired
   	Runner runner;
    @Autowired
    RadsInventoryMapper radsInventoryMapper;

    public RadsInventoryResource(RadsInventoryService radsInventoryService) {
        this.radsInventoryService = radsInventoryService;
    }

    /**
     * POST  /rads-inventories : Create a new radsInventory.
     *
     * @param radsInventoryDTO the radsInventoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new radsInventoryDTO, or with status 400 (Bad Request) if the radsInventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rads-inventories")
    @Timed
    public ResponseEntity<RadsInventoryDTO> createRadsInventory(@RequestBody RadsInventoryDTO radsInventoryDTO) throws URISyntaxException {
        log.debug("REST request to save RadsInventory : {}", radsInventoryDTO);
        if (radsInventoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new radsInventory cannot already have an ID")).body(null);
        }
        //RadsInventoryDTO result = radsInventoryService.save(radsInventoryDTO);
        
        List<RadsCategoryInventory> categoryDataList=radsCategoryInventoryService.findAll();
        ArrayList<RadsInventoryCallTask> radsInventoryCallTaskList=new ArrayList<RadsInventoryCallTask>();
        for(RadsCategoryInventory categoryData:categoryDataList){
        	radsInventoryCallTaskList.add(new RadsInventoryCallTask(categoryData.getCatId(),categoryData.getCatName()));
        }
        
        runner.fillDataForRadsInventory(radsInventoryCallTaskList);
        
        return ResponseEntity.created(new URI("/api/rads-inventories/" + 1))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, "1"))
            .body(radsInventoryDTO);
    }

    /**
     * PUT  /rads-inventories : Updates an existing radsInventory.
     *
     * @param radsInventoryDTO the radsInventoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated radsInventoryDTO,
     * or with status 400 (Bad Request) if the radsInventoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the radsInventoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rads-inventories")
    @Timed
    public ResponseEntity<RadsInventoryDTO> updateRadsInventory(@RequestBody RadsInventoryDTO radsInventoryDTO) throws URISyntaxException {
        log.debug("REST request to update RadsInventory : {}", radsInventoryDTO);
        if (radsInventoryDTO.getId() == null) {
            return createRadsInventory(radsInventoryDTO);
        }
        
        RadsInventoryDTO result = radsInventoryService.save(radsInventoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, radsInventoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rads-inventories : get all the radsInventories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of radsInventories in body
     */
    @GetMapping("/rads-inventories")
    @Timed
    public ResponseEntity<List<RadsInventoryDTO>> getAllRadsInventories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RadsInventories");
        Page<RadsInventoryDTO> page = radsInventoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rads-inventories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rads-inventories/:id : get the "id" radsInventory.
     *
     * @param id the id of the radsInventoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the radsInventoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rads-inventories/{id}")
    @Timed
    public ResponseEntity<RadsInventoryDTO> getRadsInventory(@PathVariable Long id) {
        log.debug("REST request to get RadsInventory : {}", id);
        RadsInventoryDTO radsInventoryDTO = radsInventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(radsInventoryDTO));
    }

    /**
     * DELETE  /rads-inventories/:id : delete the "id" radsInventory.
     *
     * @param id the id of the radsInventoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rads-inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRadsInventory(@PathVariable Long id) {
        log.debug("REST request to delete RadsInventory : {}", id);
        radsInventoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /rads-inventories : get all the radsInventories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of radsInventories in body
     */
    @GetMapping("/currentrads-inventories")
    @Timed
    public ResponseEntity<List<RadsInventoryDTO>> getCurrentRadsInventories() {
        log.debug("REST request to get a page of RadsInventories");
        List<RadsInventory> tuples = radsInventoryService.getDataForMaxDate(null);
        List<RadsInventoryDTO> response= new ArrayList<RadsInventoryDTO>();
        
        for (RadsInventory tuple:tuples){
        	response.add(radsInventoryMapper.toDto(tuple));
        }
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
        headersMap.set("Access-Control-Allow-Origin", "*");
       // Access-Control-Allow-Origin
         return new ResponseEntity<>(response,headersMap, HttpStatus.OK);
    }
    
    @GetMapping("/currentrads-inventories/{date}")
    @Timed
    public ResponseEntity<List<RadsInventoryDTO>> getRadsInventoriesForDate(@PathVariable LocalDate date) {
        log.debug("REST request to get a page of RadsInventories");
        List<RadsInventory> tuples = radsInventoryService.getDataForMaxDate(date);
        List<RadsInventoryDTO> response= new ArrayList<RadsInventoryDTO>();
        
        for (RadsInventory tuple:tuples){
        	response.add(radsInventoryMapper.toDto(tuple));
        }
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
        headersMap.set("Access-Control-Allow-Origin", "*");
         return new ResponseEntity<>(response,headersMap, HttpStatus.OK);
    }
    
    
    @GetMapping("/rads-inventoriesdate")
    @Timed
    public ResponseEntity<List<RadsInventoryDTO>> getRadsInventoriesBetweenDate(@RequestParam(value="toDate") LocalDate toDate, @RequestParam(value="fromDate") LocalDate fromDate) {
        log.debug("REST request to get a page of RadsInventories");
        
        
        List<RadsInventory> tuples = radsInventoryService.getDataForBetweenDate(fromDate, toDate);
        List<RadsInventoryDTO> response= new ArrayList<RadsInventoryDTO>();
        
        for (RadsInventory tuple:tuples){
        	response.add(radsInventoryMapper.toDto(tuple));
        }
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
        headersMap.set("Access-Control-Allow-Origin", "*");
         return new ResponseEntity<>(response, headersMap,HttpStatus.OK);
    }
    
    @GetMapping("/rads-categorydate")
    @Timed
    public ResponseEntity<List<RadsInventoryDTO>> getRadsInventoriesBetweenDateForCategory(@RequestParam(value="toDate") LocalDate toDate, @RequestParam(value="fromDate") LocalDate fromDate,@RequestParam(value="catId") Integer catId) {
        log.debug("REST request to get a page of RadsInventories");
        
        
        List<RadsInventory> tuples = radsInventoryService.getDataForBetweenDateForCategory(fromDate, toDate, catId);
        List<RadsInventoryDTO> response= new ArrayList<RadsInventoryDTO>();
        
        for (RadsInventory tuple:tuples){
        	response.add(radsInventoryMapper.toDto(tuple));
        }
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
        headersMap.set("Access-Control-Allow-Origin", "*");
         return new ResponseEntity<>(response, headersMap,HttpStatus.OK);
    }
    
}
