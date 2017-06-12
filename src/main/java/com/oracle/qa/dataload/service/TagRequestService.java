package com.oracle.qa.dataload.service;

import com.oracle.qa.dataload.domain.TagRequest;
import com.oracle.qa.dataload.repository.TagRequestRepository;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;
import com.oracle.qa.dataload.service.mapper.TagRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TagRequest.
 */
@Service
@Transactional
public class TagRequestService {

    private final Logger log = LoggerFactory.getLogger(TagRequestService.class);

    private final TagRequestRepository tagRequestRepository;

    private final TagRequestMapper tagRequestMapper;

    public TagRequestService(TagRequestRepository tagRequestRepository, TagRequestMapper tagRequestMapper) {
        this.tagRequestRepository = tagRequestRepository;
        this.tagRequestMapper = tagRequestMapper;
    }

    /**
     * Save a tagRequest.
     *
     * @param tagRequestDTO the entity to save
     * @return the persisted entity
     */
    public TagRequestDTO save(TagRequestDTO tagRequestDTO) {
        log.debug("Request to save TagRequest : {}", tagRequestDTO);
        TagRequest tagRequest = tagRequestMapper.toEntity(tagRequestDTO);
        tagRequest = tagRequestRepository.save(tagRequest);
        return tagRequestMapper.toDto(tagRequest);
    }

    /**
     *  Get all the tagRequests.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TagRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TagRequests");
        return tagRequestRepository.findAll(pageable)
            .map(tagRequestMapper::toDto);
    }

    /**
     *  Get one tagRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TagRequestDTO findOne(Long id) {
        log.debug("Request to get TagRequest : {}", id);
        TagRequest tagRequest = tagRequestRepository.findOne(id);
        return tagRequestMapper.toDto(tagRequest);
    }

    /**
     *  Delete the  tagRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TagRequest : {}", id);
        tagRequestRepository.delete(id);
    }
}
