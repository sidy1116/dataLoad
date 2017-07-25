package com.oracle.qa.dataload.service.mapper;

import org.mapstruct.Mapper;

import com.oracle.qa.dataload.domain.TagRequest;
import com.oracle.qa.dataload.service.dto.TagRequestDTO;

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
