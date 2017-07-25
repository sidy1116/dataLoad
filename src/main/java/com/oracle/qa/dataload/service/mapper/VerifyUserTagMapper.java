package com.oracle.qa.dataload.service.mapper;

import org.mapstruct.Mapper;

import com.oracle.qa.dataload.domain.VerifyUserTag;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;

/**
 * Mapper for the entity VerifyUserTag and its DTO VerifyUserTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VerifyUserTagMapper extends EntityMapper <VerifyUserTagDTO, VerifyUserTag> {
    
    
    default VerifyUserTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        VerifyUserTag verifyUserTag = new VerifyUserTag();
        verifyUserTag.setId(id);
        return verifyUserTag;
    }
}
