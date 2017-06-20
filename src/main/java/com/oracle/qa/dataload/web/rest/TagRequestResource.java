package com.oracle.qa.dataload.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
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
import com.oracle.qa.dataload.service.TagRequestService;
import com.oracle.qa.dataload.service.UserService;
import com.oracle.qa.dataload.service.async.tasks.TagCallTask;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;
import com.oracle.qa.dataload.service.executors.Runner;
import com.oracle.qa.dataload.web.rest.util.HeaderUtil;
import com.oracle.qa.dataload.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing TagRequest.
 */
@RestController
@RequestMapping("/api")
public class TagRequestResource {

    private final Logger log = LoggerFactory.getLogger(TagRequestResource.class);

    private static final String ENTITY_NAME = "tagRequest";
    
   // private static final String TAG_URL="tags.bluekai.com/site/";

    private final TagRequestService tagRequestService;
    
    @Autowired
	Runner runner;
   
  
    public TagRequestResource(TagRequestService tagRequestService) {
        this.tagRequestService = tagRequestService;
    }

    /**
     * POST  /tag-requests : Create a new tagRequest.
     *
     * @param tagRequestDTO the tagRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tagRequestDTO, or with status 400 (Bad Request) if the tagRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException 
     */
    @PostMapping("/tag-requests")
    @Timed
    public ResponseEntity<TagRequestDTO> createTagRequest(@Valid @RequestBody TagRequestDTO tagRequestDTO) throws URISyntaxException, IOException {
        log.debug("REST request to save TagRequest : {}", tagRequestDTO);
        if (tagRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tagRequest cannot already have an ID")).body(null);
        }
        tagRequestDTO.setCreateDate(LocalDate.now());
        TagRequestDTO result = tagRequestService.save(tagRequestDTO);
        /**
         * code for Aysnc
         */
       

        ArrayList<TagCallTask> tagCallArrayList=new ArrayList<TagCallTask>();
        for (int i=0;i<tagRequestDTO.getRequestCount();i++){
        	tagCallArrayList.add(new TagCallTask(tagRequestDTO.getSiteId(),tagRequestDTO.getPhints(),tagRequestDTO.getHeaders(),tagRequestDTO.getIdType()));
        }
        runner.useCompletableFutureWithExecutor(tagCallArrayList,result);
        /**
         * 
         */
        
        return ResponseEntity.created(new URI("/api/tag-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tag-requests : Updates an existing tagRequest.
     *
     * @param tagRequestDTO the tagRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tagRequestDTO,
     * or with status 400 (Bad Request) if the tagRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the tagRequestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException 
     */
    @PutMapping("/tag-requests")
    @Timed
    public ResponseEntity<TagRequestDTO> updateTagRequest(@Valid @RequestBody TagRequestDTO tagRequestDTO) throws URISyntaxException, IOException {
        log.debug("REST request to update TagRequest : {}", tagRequestDTO);
        if (tagRequestDTO.getId() == null) {
            return createTagRequest(tagRequestDTO);
        }
        TagRequestDTO result = tagRequestService.save(tagRequestDTO);
        /**
         * code for Aysnc
         */
        ArrayList<TagCallTask> tagCallArrayList=new ArrayList<TagCallTask>();
        for (int i=0;i<tagRequestDTO.getRequestCount();i++){
        	tagCallArrayList.add(new TagCallTask(tagRequestDTO.getSiteId(),tagRequestDTO.getPhints(),tagRequestDTO.getHeaders(),tagRequestDTO.getIdType()));
        }
        runner.useCompletableFutureWithExecutor(tagCallArrayList, result);
        /**
         * 
         */
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tagRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tag-requests : get all the tagRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tagRequests in body
     */
    @GetMapping("/tag-requests")
    @Timed
    public ResponseEntity<List<TagRequestDTO>> getAllTagRequests(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TagRequests");
        Page<TagRequestDTO> page = tagRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tag-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tag-requests/:id : get the "id" tagRequest.
     *
     * @param id the id of the tagRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tagRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tag-requests/{id}")
    @Timed
    public ResponseEntity<TagRequestDTO> getTagRequest(@PathVariable Long id) {
        log.debug("REST request to get TagRequest : {}", id);
        TagRequestDTO tagRequestDTO = tagRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tagRequestDTO));
    }

    /**
     * DELETE  /tag-requests/:id : delete the "id" tagRequest.
     *
     * @param id the id of the tagRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tag-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteTagRequest(@PathVariable Long id) {
        log.debug("REST request to delete TagRequest : {}", id);
        tagRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
