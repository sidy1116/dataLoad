package com.oracle.qa.dataload.service.mapper;

import org.mapstruct.Mapper;

import com.oracle.qa.dataload.domain.ReTagProfile;
import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;

/**
 * Mapper for the entity ReTagProfile and its DTO ReTagProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReTagProfileMapper extends EntityMapper <ReTagProfileDTO, ReTagProfile> {
    
    
    default ReTagProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReTagProfile reTagProfile = new ReTagProfile();
        reTagProfile.setId(id);
        return reTagProfile;
    }
}
