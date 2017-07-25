package com.oracle.qa.dataload.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.qa.dataload.domain.RadsCategoryInventory;
import com.oracle.qa.dataload.repository.RadsCategoryInventoryRepository;
import com.oracle.qa.dataload.service.dto.RadsCategoryInventoryDTO;
import com.oracle.qa.dataload.service.mapper.RadsCategoryInventoryMapper;


/**
 * Service Implementation for managing RadsCategoryInventory.
 */
@Service
@Transactional
public class RadsCategoryInventoryService {

    private final Logger log = LoggerFactory.getLogger(RadsCategoryInventoryService.class);

    private final RadsCategoryInventoryRepository radsCategoryInventoryRepository;

    @Autowired
    private  RadsCategoryInventoryMapper radsCategoryInventoryMapper;

    public RadsCategoryInventoryService(RadsCategoryInventoryRepository radsCategoryInventoryRepository, RadsCategoryInventoryMapper radsCategoryInventoryMapper) {
        this.radsCategoryInventoryRepository = radsCategoryInventoryRepository;
        this.radsCategoryInventoryMapper = radsCategoryInventoryMapper;
    }

    /**
     * Save a radsCategoryInventory.
     *
     * @param radsCategoryInventoryDTO the entity to save
     * @return the persisted entity
     */
    public RadsCategoryInventoryDTO save(RadsCategoryInventoryDTO radsCategoryInventoryDTO) {
        log.debug("Request to save RadsCategoryInventory : {}", radsCategoryInventoryDTO);
        RadsCategoryInventory radsCategoryInventory = radsCategoryInventoryMapper.toEntity(radsCategoryInventoryDTO);
        radsCategoryInventory = radsCategoryInventoryRepository.save(radsCategoryInventory);
        return radsCategoryInventoryMapper.toDto(radsCategoryInventory);
    }

    /**
     *  Get all the radsCategoryInventories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RadsCategoryInventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RadsCategoryInventories");
        return radsCategoryInventoryRepository.findAll(pageable)
            .map(radsCategoryInventoryMapper::toDto);
    }

    /**
     *  Get one radsCategoryInventory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RadsCategoryInventoryDTO findOne(Long id) {
        log.debug("Request to get RadsCategoryInventory : {}", id);
        RadsCategoryInventory radsCategoryInventory = radsCategoryInventoryRepository.findOne(id);
        return radsCategoryInventoryMapper.toDto(radsCategoryInventory);
    }

    /**
     *  Delete the  radsCategoryInventory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RadsCategoryInventory : {}", id);
        radsCategoryInventoryRepository.delete(id);
    }

	public List<RadsCategoryInventory> findAll() {
		// TODO Auto-generated method stub
		log.debug("Request to get all RadsCategoryInventories");
		return radsCategoryInventoryRepository.findAll();
	}
}
