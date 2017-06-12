package com.oracle.qa.dataload.service.mapper;

import com.oracle.qa.dataload.domain.*;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TagRequest and its DTO TagRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagRequestMapper extends EntityMapper <TagRequestDTO, TagRequest> {
    
    
    default TagRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        TagRequest tagRequest = new TagRequest();
        tagRequest.setId(id);
        return tagRequest;
    }
}
