package com.oracle.qa.dataload.service.mapper;

import com.oracle.qa.dataload.domain.TagRequest;

import com.oracle.qa.dataload.service.dto.TagRequestDTO;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;

@Generated(

    value = "org.mapstruct.ap.MappingProcessor",

    date = "2017-07-05T10:11:44+0530",

    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_91 (Oracle Corporation)"

)

@Component

public class TagRequestMapperImpl implements TagRequestMapper {

    @Override

    public TagRequest toEntity(TagRequestDTO dto) {

        if ( dto == null ) {

            return null;
        }

        TagRequest tagRequest = new TagRequest();

        tagRequest.setId( dto.getId() );

        tagRequest.setSiteId( dto.getSiteId() );

        tagRequest.setPhints( dto.getPhints() );

        tagRequest.setReferelUrl( dto.getReferelUrl() );

        tagRequest.setHeaders( dto.getHeaders() );

        tagRequest.setIdType( dto.getIdType() );

        tagRequest.setRequestCount( dto.getRequestCount() );

        if ( dto.getFile() != null ) {

            byte[] file = dto.getFile();

            tagRequest.setFile( Arrays.copyOf( file, file.length ) );
        }

        tagRequest.setFileContentType( dto.getFileContentType() );

        tagRequest.setCreateDate( dto.getCreateDate() );

        return tagRequest;
    }

    @Override

    public TagRequestDTO toDto(TagRequest entity) {

        if ( entity == null ) {

            return null;
        }

        TagRequestDTO tagRequestDTO = new TagRequestDTO();

        tagRequestDTO.setId( entity.getId() );

        tagRequestDTO.setSiteId( entity.getSiteId() );

        tagRequestDTO.setPhints( entity.getPhints() );

        tagRequestDTO.setReferelUrl( entity.getReferelUrl() );

        tagRequestDTO.setHeaders( entity.getHeaders() );

        tagRequestDTO.setIdType( entity.getIdType() );

        tagRequestDTO.setRequestCount( entity.getRequestCount() );

        if ( entity.getFile() != null ) {

            byte[] file = entity.getFile();

            tagRequestDTO.setFile( Arrays.copyOf( file, file.length ) );
        }

        tagRequestDTO.setFileContentType( entity.getFileContentType() );

        tagRequestDTO.setCreateDate( entity.getCreateDate() );

        return tagRequestDTO;
    }

    @Override

    public List<TagRequest> toEntity(List<TagRequestDTO> dtoList) {

        if ( dtoList == null ) {

            return null;
        }

        List<TagRequest> list = new ArrayList<TagRequest>();

        for ( TagRequestDTO tagRequestDTO : dtoList ) {

            list.add( toEntity( tagRequestDTO ) );
        }

        return list;
    }

    @Override

    public List<TagRequestDTO> toDto(List<TagRequest> entityList) {

        if ( entityList == null ) {

            return null;
        }

        List<TagRequestDTO> list = new ArrayList<TagRequestDTO>();

        for ( TagRequest tagRequest : entityList ) {

            list.add( toDto( tagRequest ) );
        }

        return list;
    }
}

