package com.oracle.qa.dataload.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.oracle.qa.dataload.service.RadsCategoryInventoryService;
import com.oracle.qa.dataload.service.dto.RadsCategoryInventoryDTO;
import com.oracle.qa.dataload.web.rest.util.HeaderUtil;
import com.oracle.qa.dataload.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing RadsCategoryInventory.
 */
@RestController
@RequestMapping("/api")
public class RadsCategoryInventoryResource {

    private final Logger log = LoggerFactory.getLogger(RadsCategoryInventoryResource.class);

    private static final String ENTITY_NAME = "radsCategoryInventory";

    private final RadsCategoryInventoryService radsCategoryInventoryService;

    public RadsCategoryInventoryResource(RadsCategoryInventoryService radsCategoryInventoryService) {
        this.radsCategoryInventoryService = radsCategoryInventoryService;
    }

    /**
     * POST  /rads-category-inventories : Create a new radsCategoryInventory.
     *
     * @param radsCategoryInventoryDTO the radsCategoryInventoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new radsCategoryInventoryDTO, or with status 400 (Bad Request) if the radsCategoryInventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rads-category-inventories")
    @Timed
    public ResponseEntity<RadsCategoryInventoryDTO> createRadsCategoryInventory(@Valid @RequestBody RadsCategoryInventoryDTO radsCategoryInventoryDTO) throws URISyntaxException {
        log.debug("REST request to save RadsCategoryInventory : {}", radsCategoryInventoryDTO);
        if (radsCategoryInventoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new radsCategoryInventory cannot already have an ID")).body(null);
        }
        RadsCategoryInventoryDTO result = radsCategoryInventoryService.save(radsCategoryInventoryDTO);
        return ResponseEntity.created(new URI("/api/rads-category-inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rads-category-inventories : Updates an existing radsCategoryInventory.
     *
     * @param radsCategoryInventoryDTO the radsCategoryInventoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated radsCategoryInventoryDTO,
     * or with status 400 (Bad Request) if the radsCategoryInventoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the radsCategoryInventoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rads-category-inventories")
    @Timed
    public ResponseEntity<RadsCategoryInventoryDTO> updateRadsCategoryInventory(@Valid @RequestBody RadsCategoryInventoryDTO radsCategoryInventoryDTO) throws URISyntaxException {
        log.debug("REST request to update RadsCategoryInventory : {}", radsCategoryInventoryDTO);
        if (radsCategoryInventoryDTO.getId() == null) {
            return createRadsCategoryInventory(radsCategoryInventoryDTO);
        }
        RadsCategoryInventoryDTO result = radsCategoryInventoryService.save(radsCategoryInventoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, radsCategoryInventoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rads-category-inventories : get all the radsCategoryInventories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of radsCategoryInventories in body
     */
    @GetMapping("/rads-category-inventories")
    @Timed
    public ResponseEntity<List<RadsCategoryInventoryDTO>> getAllRadsCategoryInventories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RadsCategoryInventories");
        Page<RadsCategoryInventoryDTO> page = radsCategoryInventoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rads-category-inventories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rads-category-inventories/:id : get the "id" radsCategoryInventory.
     *
     * @param id the id of the radsCategoryInventoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the radsCategoryInventoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rads-category-inventories/{id}")
    @Timed
    public ResponseEntity<RadsCategoryInventoryDTO> getRadsCategoryInventory(@PathVariable Long id) {
        log.debug("REST request to get RadsCategoryInventory : {}", id);
        RadsCategoryInventoryDTO radsCategoryInventoryDTO = radsCategoryInventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(radsCategoryInventoryDTO));
    }

    /**
     * DELETE  /rads-category-inventories/:id : delete the "id" radsCategoryInventory.
     *
     * @param id the id of the radsCategoryInventoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rads-category-inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRadsCategoryInventory(@PathVariable Long id) {
        log.debug("REST request to delete RadsCategoryInventory : {}", id);
        radsCategoryInventoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
