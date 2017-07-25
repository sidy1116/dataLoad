package com.oracle.qa.dataload.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.qa.dataload.domain.RadsInventory;
import com.oracle.qa.dataload.repository.RadsInventoryRepository;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;
import com.oracle.qa.dataload.service.mapper.RadsInventoryMapper;


/**
 * Service Implementation for managing RadsInventory.
 */
@Service
@Transactional
public class RadsInventoryService {

    private final Logger log = LoggerFactory.getLogger(RadsInventoryService.class);

    private final RadsInventoryRepository radsInventoryRepository;

    private final RadsInventoryMapper radsInventoryMapper;

    public RadsInventoryService(RadsInventoryRepository radsInventoryRepository, RadsInventoryMapper radsInventoryMapper) {
        this.radsInventoryRepository = radsInventoryRepository;
        this.radsInventoryMapper = radsInventoryMapper;
    }

    /**
     * Save a radsInventory.
     *
     * @param radsInventoryDTO the entity to save
     * @return the persisted entity
     */
    public RadsInventoryDTO save(RadsInventoryDTO radsInventoryDTO) {
        log.debug("Request to save RadsInventory : {}", radsInventoryDTO);
        RadsInventory radsInventory = radsInventoryMapper.toEntity(radsInventoryDTO);
        radsInventory = radsInventoryRepository.save(radsInventory);
        return radsInventoryMapper.toDto(radsInventory);
    }

    /**
     *  Get all the radsInventories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RadsInventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RadsInventories");
        return radsInventoryRepository.findAll(pageable)
            .map(radsInventoryMapper::toDto);
    }

    /**
     *  Get one radsInventory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RadsInventoryDTO findOne(Long id) {
        log.debug("Request to get RadsInventory : {}", id);
        RadsInventory radsInventory = radsInventoryRepository.findOne(id);
        return radsInventoryMapper.toDto(radsInventory);
    }

    /**
     *  Delete the  radsInventory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RadsInventory : {}", id);
        radsInventoryRepository.delete(id);
    }
    
    public LocalDate getMaxDate() {
    	return radsInventoryRepository.findMaxDate();
    }
    
    public List<RadsInventory> getDataForMaxDate(LocalDate date) {
    	if (date==null) date=radsInventoryRepository.findMaxDate();
    	return (List<RadsInventory>) radsInventoryRepository.findRecodsForMaxDate(date);
    }

	public List<RadsInventory> getDataForBetweenDate(LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		 return (List<RadsInventory>) radsInventoryRepository.findDataForBetweenDate(fromDate,toDate);
	}
	public List<RadsInventory> getDataForBetweenDateForCategory(LocalDate fromDate, LocalDate toDate,Integer catId) {
		// TODO Auto-generated method stub
		 return (List<RadsInventory>) radsInventoryRepository.findDataForBetweenDateForCatId(fromDate,toDate,catId);
	}
}
