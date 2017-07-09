package com.oracle.qa.dataload.service;

import com.oracle.qa.dataload.domain.VerifyUserTag;
import com.oracle.qa.dataload.repository.VerifyUserTagRepository;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;
import com.oracle.qa.dataload.service.mapper.VerifyUserTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing VerifyUserTag.
 */
@Service
@Transactional
public class VerifyUserTagService {

    private final Logger log = LoggerFactory.getLogger(VerifyUserTagService.class);

    private final VerifyUserTagRepository verifyUserTagRepository;

    private final VerifyUserTagMapper verifyUserTagMapper;

    public VerifyUserTagService(VerifyUserTagRepository verifyUserTagRepository, VerifyUserTagMapper verifyUserTagMapper) {
        this.verifyUserTagRepository = verifyUserTagRepository;
        this.verifyUserTagMapper = verifyUserTagMapper;
    }

    /**
     * Save a verifyUserTag.
     *
     * @param verifyUserTagDTO the entity to save
     * @return the persisted entity
     */
    public VerifyUserTagDTO save(VerifyUserTagDTO verifyUserTagDTO) {
        log.debug("Request to save VerifyUserTag : {}", verifyUserTagDTO);
        VerifyUserTag verifyUserTag = verifyUserTagMapper.toEntity(verifyUserTagDTO);
        verifyUserTag = verifyUserTagRepository.save(verifyUserTag);
        return verifyUserTagMapper.toDto(verifyUserTag);
    }

    /**
     *  Get all the verifyUserTags.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VerifyUserTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VerifyUserTags");
        return verifyUserTagRepository.findAll(pageable)
            .map(verifyUserTagMapper::toDto);
    }

    /**
     *  Get one verifyUserTag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VerifyUserTagDTO findOne(Long id) {
        log.debug("Request to get VerifyUserTag : {}", id);
        VerifyUserTag verifyUserTag = verifyUserTagRepository.findOne(id);
        return verifyUserTagMapper.toDto(verifyUserTag);
    }

    /**
     *  Delete the  verifyUserTag by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete VerifyUserTag : {}", id);
        verifyUserTagRepository.delete(id);
    }
}
