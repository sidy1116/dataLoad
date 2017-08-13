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
import com.oracle.qa.dataload.domain.enumeration.IdType;
import com.oracle.qa.dataload.domain.enumeration.Status;
import com.oracle.qa.dataload.service.ReTagProfileService;
import com.oracle.qa.dataload.service.async.tasks.TagCallTask;
import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;
import com.oracle.qa.dataload.service.executors.Runner;
import com.oracle.qa.dataload.web.rest.util.HeaderUtil;
import com.oracle.qa.dataload.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing ReTagProfile.
 */
@RestController
@RequestMapping("/api")
public class ReTagProfileResource {

	private final Logger log = LoggerFactory.getLogger(ReTagProfileResource.class);

	private static final String ENTITY_NAME = "reTagProfile";

	private final ReTagProfileService reTagProfileService;
	@Autowired
	Runner runner;

	public ReTagProfileResource(ReTagProfileService reTagProfileService) {
		this.reTagProfileService = reTagProfileService;
	}

	/**
	 * POST /re-tag-profiles : Create a new reTagProfile.
	 *
	 * @param reTagProfileDTO
	 *            the reTagProfileDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new reTagProfileDTO, or with status 400 (Bad Request) if the
	 *         reTagProfile has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/re-tag-profiles")
	@Timed
	public ResponseEntity<ReTagProfileDTO> createReTagProfile(@Valid @RequestBody ReTagProfileDTO reTagProfileDTO)
			throws URISyntaxException {

		log.debug("REST request to save ReTagProfile : {}", reTagProfileDTO);

		if (reTagProfileDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new reTagProfile cannot already have an ID")).body(null);
		}

		reTagProfileDTO.setCreateDate(LocalDate.now());
		reTagProfileDTO.setReTagCount(0);
		reTagProfileDTO.setStatus(Status.ACTIVE);
		String[] inputFile = new String(reTagProfileDTO.getInputFile(), StandardCharsets.UTF_8).split("\n");

		ArrayList<TagCallTask> tagCallArrayList = new ArrayList<TagCallTask>();
		if (reTagProfileDTO.getStartFromLine() == null)
			reTagProfileDTO.setStartFromLine(0);

		if (reTagProfileDTO.getToLine() == null)
			reTagProfileDTO.setToLine(inputFile.length);

		 int from =reTagProfileDTO.getStartFromLine();
		 int to=reTagProfileDTO.getToLine();
			ReTagProfileDTO result = reTagProfileService.save(reTagProfileDTO);
			reTagProfileDTO.setId(result.getId());
		 if(from==1)from=0;
		 
		 for(int i=from;i<to;i++){
			 String completeHeader = "";
			 String retagbkuid=inputFile[i];
				if (reTagProfileDTO.getHeaders() != null) {
					completeHeader = "Cookie:bku=" + retagbkuid + "||" + reTagProfileDTO.getHeaders();
				} else
					completeHeader = "Cookie:bku=" + retagbkuid;
			TagCallTask tagcalltask = new TagCallTask(reTagProfileDTO.getSiteId(), reTagProfileDTO.getPhint(),
					completeHeader,IdType.bkuuid);
				tagCallArrayList.add(tagcalltask);
		 }


		try {
			runner.reTagWithCompletableFutureExecutor(tagCallArrayList, reTagProfileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.created(new URI("/api/re-tag-profiles/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /re-tag-profiles : Updates an existing reTagProfile.
	 *
	 * @param reTagProfileDTO
	 *            the reTagProfileDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         reTagProfileDTO, or with status 400 (Bad Request) if the
	 *         reTagProfileDTO is not valid, or with status 500 (Internal Server
	 *         Error) if the reTagProfileDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/re-tag-profiles")
	@Timed
	public ResponseEntity<ReTagProfileDTO> updateReTagProfile(@Valid @RequestBody ReTagProfileDTO reTagProfileDTO)
			throws URISyntaxException {
		log.debug("REST request to update ReTagProfile : {}", reTagProfileDTO);
		if (reTagProfileDTO.getId() == null) {
			return createReTagProfile(reTagProfileDTO);
		}
		ReTagProfileDTO result = reTagProfileService.save(reTagProfileDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reTagProfileDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /re-tag-profiles : get all the reTagProfiles.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         reTagProfiles in body
	 */
	@GetMapping("/re-tag-profiles")
	@Timed
	public ResponseEntity<List<ReTagProfileDTO>> getAllReTagProfiles(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of ReTagProfiles");
		Page<ReTagProfileDTO> page = reTagProfileService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/re-tag-profiles");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	

	/**
	 * GET /re-tag-profiles/:id : get the "id" reTagProfile.
	 *
	 * @param id
	 *            the id of the reTagProfileDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         reTagProfileDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/re-tag-profiles/{id}")
	@Timed
	public ResponseEntity<ReTagProfileDTO> getReTagProfile(@PathVariable Long id) {
		log.debug("REST request to get ReTagProfile : {}", id);
		ReTagProfileDTO reTagProfileDTO = reTagProfileService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reTagProfileDTO));
	}

	@GetMapping("/re-tag-profiles-file/{id}")
	@Timed
	public ResponseEntity<ReTagProfileDTO> getReTagProfileFile(@PathVariable Long id) {
		log.debug("REST request to get ReTagProfile : {}", id);
		ReTagProfileDTO reTagProfileDTO = reTagProfileService.findTagRequestFile(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reTagProfileDTO));
	}
	
	/**
	 * DELETE /re-tag-profiles/:id : delete the "id" reTagProfile.
	 *
	 * @param id
	 *            the id of the reTagProfileDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/re-tag-profiles/{id}")
	@Timed
	public ResponseEntity<Void> deleteReTagProfile(@PathVariable Long id) {
		log.debug("REST request to delete ReTagProfile : {}", id);
		reTagProfileService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
