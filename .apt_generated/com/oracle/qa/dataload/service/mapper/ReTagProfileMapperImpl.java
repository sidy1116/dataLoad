package com.oracle.qa.dataload.service.mapper;

import com.oracle.qa.dataload.domain.ReTagProfile;

import com.oracle.qa.dataload.service.dto.ReTagProfileDTO;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;

@Generated(

    value = "org.mapstruct.ap.MappingProcessor",

    date = "2017-06-15T21:07:27+0530",

    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_91 (Oracle Corporation)"

)

@Component

public class ReTagProfileMapperImpl implements ReTagProfileMapper {

    @Override

    public ReTagProfile toEntity(ReTagProfileDTO dto) {

        if ( dto == null ) {

            return null;
        }

        ReTagProfile reTagProfile = new ReTagProfile();

        reTagProfile.setId( dto.getId() );

        reTagProfile.setSiteId( dto.getSiteId() );

        if ( dto.getInputFile() != null ) {

            byte[] inputFile = dto.getInputFile();

            reTagProfile.setInputFile( Arrays.copyOf( inputFile, inputFile.length ) );
        }

        reTagProfile.setInputFileContentType( dto.getInputFileContentType() );

        reTagProfile.setPhint( dto.getPhint() );

        reTagProfile.setHeaders( dto.getHeaders() );

        reTagProfile.setCreateDate( dto.getCreateDate() );

        reTagProfile.setStartFromLine( dto.getStartFromLine() );

        reTagProfile.setToLine( dto.getToLine() );

        return reTagProfile;
    }

    @Override

    public ReTagProfileDTO toDto(ReTagProfile entity) {

        if ( entity == null ) {

            return null;
        }

        ReTagProfileDTO reTagProfileDTO = new ReTagProfileDTO();

        reTagProfileDTO.setId( entity.getId() );

        reTagProfileDTO.setSiteId( entity.getSiteId() );

        if ( entity.getInputFile() != null ) {

            byte[] inputFile = entity.getInputFile();

            reTagProfileDTO.setInputFile( Arrays.copyOf( inputFile, inputFile.length ) );
        }

        reTagProfileDTO.setInputFileContentType( entity.getInputFileContentType() );

        reTagProfileDTO.setPhint( entity.getPhint() );

        reTagProfileDTO.setHeaders( entity.getHeaders() );

        reTagProfileDTO.setCreateDate( entity.getCreateDate() );

        reTagProfileDTO.setStartFromLine( entity.getStartFromLine() );

        reTagProfileDTO.setToLine( entity.getToLine() );

        return reTagProfileDTO;
    }

    @Override

    public List<ReTagProfile> toEntity(List<ReTagProfileDTO> dtoList) {

        if ( dtoList == null ) {

            return null;
        }

        List<ReTagProfile> list = new ArrayList<ReTagProfile>();

        for ( ReTagProfileDTO reTagProfileDTO : dtoList ) {

            list.add( toEntity( reTagProfileDTO ) );
        }

        return list;
    }

    @Override

    public List<ReTagProfileDTO> toDto(List<ReTagProfile> entityList) {

        if ( entityList == null ) {

            return null;
        }

        List<ReTagProfileDTO> list = new ArrayList<ReTagProfileDTO>();

        for ( ReTagProfile reTagProfile : entityList ) {

            list.add( toDto( reTagProfile ) );
        }

        return list;
    }
}

