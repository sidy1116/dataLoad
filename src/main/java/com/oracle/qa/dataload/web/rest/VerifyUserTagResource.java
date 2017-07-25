package com.oracle.qa.dataload.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oracle.qa.dataload.service.VerifyUserTagService;
import com.oracle.qa.dataload.service.async.tasks.VerifyBkuidTask;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;
import com.oracle.qa.dataload.service.executors.Runner;
import com.oracle.qa.dataload.web.rest.util.HeaderUtil;
import com.oracle.qa.dataload.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing VerifyUserTag.
 */
@RestController
@RequestMapping("/api")
public class VerifyUserTagResource {

    private final Logger log = LoggerFactory.getLogger(VerifyUserTagResource.class);

    private static final String ENTITY_NAME = "verifyUserTag";

    private final VerifyUserTagService verifyUserTagService;

    public VerifyUserTagResource(VerifyUserTagService verifyUserTagService) {
        this.verifyUserTagService = verifyUserTagService;
    }
    @Autowired
	Runner runner;
    /**
     * POST  /verify-user-tags : Create a new verifyUserTag.
     *
     * @param verifyUserTagDTO the verifyUserTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new verifyUserTagDTO, or with status 400 (Bad Request) if the verifyUserTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/verify-user-tags")
    @Timed
    public ResponseEntity<VerifyUserTagDTO> createVerifyUserTag(@Valid @RequestBody VerifyUserTagDTO verifyUserTagDTO) throws URISyntaxException {
        log.debug("REST request to save VerifyUserTag : {}", verifyUserTagDTO);
        if (verifyUserTagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new verifyUserTag cannot already have an ID")).body(null);
        }
        verifyUserTagDTO.setVerifyDate(LocalDate.now());
        String[] inputFile = new String(verifyUserTagDTO.getInputFile(), StandardCharsets.UTF_8).split("\n");
        ArrayList<VerifyBkuidTask> verifyBkuidTaskList = new ArrayList<VerifyBkuidTask>();
        
        if(verifyUserTagDTO.getStartFrom()==null)
        	verifyUserTagDTO.setStartFrom(0);
        
        if (verifyUserTagDTO.getToLine() == null)
        	verifyUserTagDTO.setToLine(inputFile.length);
        
        int from =verifyUserTagDTO.getStartFrom();
		 int to=verifyUserTagDTO.getToLine();
		 
		 if(from==1)from=0;
		 
		 
		 for(int i=from;i<to;i++){
			 String verifybkuid=inputFile[i];				
			 VerifyBkuidTask verifyBkuidTask = new VerifyBkuidTask(verifybkuid, "siddchou", "JmwOBmjHMG23",verifyUserTagDTO.getCategoryId());
			 verifyBkuidTaskList.add(verifyBkuidTask);
		 }

		 
        
        
        VerifyUserTagDTO result = verifyUserTagService.save(verifyUserTagDTO);
        
        try {
			runner.useVerifyBkuidTaskCompletableFutureWithExecutor(verifyBkuidTaskList, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return ResponseEntity.created(new URI("/api/verify-user-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /verify-user-tags : Updates an existing verifyUserTag.
     *
     * @param verifyUserTagDTO the verifyUserTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated verifyUserTagDTO,
     * or with status 400 (Bad Request) if the verifyUserTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the verifyUserTagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/verify-user-tags")
    @Timed
    public ResponseEntity<VerifyUserTagDTO> updateVerifyUserTag(@Valid @RequestBody VerifyUserTagDTO verifyUserTagDTO) throws URISyntaxException {
        log.debug("REST request to update VerifyUserTag : {}", verifyUserTagDTO);
        if (verifyUserTagDTO.getId() == null) {
            return createVerifyUserTag(verifyUserTagDTO);
        }
        VerifyUserTagDTO result = verifyUserTagService.save(verifyUserTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, verifyUserTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /verify-user-tags : get all the verifyUserTags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of verifyUserTags in body
     */
    @GetMapping("/verify-user-tags")
    @Timed
    public ResponseEntity<List<VerifyUserTagDTO>> getAllVerifyUserTags(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of VerifyUserTags");
        Page<VerifyUserTagDTO> page = verifyUserTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/verify-user-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /verify-user-tags/:id : get the "id" verifyUserTag.
     *
     * @param id the id of the verifyUserTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the verifyUserTagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/verify-user-tags/{id}")
    @Timed
    public ResponseEntity<VerifyUserTagDTO> getVerifyUserTag(@PathVariable Long id) {
        log.debug("REST request to get VerifyUserTag : {}", id);
        VerifyUserTagDTO verifyUserTagDTO = verifyUserTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(verifyUserTagDTO));
    }

    /**
     * DELETE  /verify-user-tags/:id : delete the "id" verifyUserTag.
     *
     * @param id the id of the verifyUserTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/verify-user-tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteVerifyUserTag(@PathVariable Long id) {
        log.debug("REST request to delete VerifyUserTag : {}", id);
        verifyUserTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
