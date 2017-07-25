package com.oracle.qa.dataload.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import com.oracle.qa.dataload.domain.RadsCategoryInventory;
import com.oracle.qa.dataload.service.dto.RadsCategoryInventoryDTO;

/**
 * Mapper for the entity RadsCategoryInventory and its DTO RadsCategoryInventoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@Service
public interface RadsCategoryInventoryMapper extends EntityMapper <RadsCategoryInventoryDTO, RadsCategoryInventory> {
    
    
    default RadsCategoryInventory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RadsCategoryInventory radsCategoryInventory = new RadsCategoryInventory();
        radsCategoryInventory.setId(id);
        return radsCategoryInventory;
    }
}
