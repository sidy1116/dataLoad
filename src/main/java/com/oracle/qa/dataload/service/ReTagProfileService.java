package com.oracle.qa.dataload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.qa.dataload.domain.ReTagProfile;
import com.oracle.qa.dataload.repository.ReTagProfileRepository;
import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;
import com.oracle.qa.dataload.service.mapper.ReTagProfileMapper;


/**
 * Service Implementation for managing ReTagProfile.
 */
@Service
@Transactional
public class ReTagProfileService {

    private final Logger log = LoggerFactory.getLogger(ReTagProfileService.class);

    private final ReTagProfileRepository reTagProfileRepository;

    private final ReTagProfileMapper reTagProfileMapper;

    public ReTagProfileService(ReTagProfileRepository reTagProfileRepository, ReTagProfileMapper reTagProfileMapper) {
        this.reTagProfileRepository = reTagProfileRepository;
        this.reTagProfileMapper = reTagProfileMapper;
    }

    /**
     * Save a reTagProfile.
     *
     * @param reTagProfileDTO the entity to save
     * @return the persisted entity
     */
    public ReTagProfileDTO save(ReTagProfileDTO reTagProfileDTO) {
        log.debug("Request to save ReTagProfile : {}", reTagProfileDTO);
        ReTagProfile reTagProfile = reTagProfileMapper.toEntity(reTagProfileDTO);
        reTagProfile = reTagProfileRepository.save(reTagProfile);
        return reTagProfileMapper.toDto(reTagProfile);
    }

    /**
     *  Get all the reTagProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReTagProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReTagProfiles");
        return reTagProfileRepository.findAll(pageable)
            .map(reTagProfileMapper::toDto);
    }

    /**
     *  Get one reTagProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReTagProfileDTO findOne(Long id) {
        log.debug("Request to get ReTagProfile : {}", id);
        ReTagProfile reTagProfile = reTagProfileRepository.findOne(id);
        return reTagProfileMapper.toDto(reTagProfile);
    }

    /**
     *  Delete the  reTagProfile by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReTagProfile : {}", id);
        reTagProfileRepository.delete(id);
    }
}
