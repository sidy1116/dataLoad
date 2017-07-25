package com.oracle.qa.dataload.service.mapper;

import org.mapstruct.Mapper;

import com.oracle.qa.dataload.domain.RadsInventory;
import com.oracle.qa.dataload.service.dto.RadsInventoryDTO;

/**
 * Mapper for the entity RadsInventory and its DTO RadsInventoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RadsInventoryMapper extends EntityMapper <RadsInventoryDTO, RadsInventory> {
    
    
    default RadsInventory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RadsInventory radsInventory = new RadsInventory();
        radsInventory.setId(id);
        return radsInventory;
    }
}
