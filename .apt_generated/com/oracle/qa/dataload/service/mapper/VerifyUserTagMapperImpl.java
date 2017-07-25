package com.oracle.qa.dataload.service.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;

import com.oracle.qa.dataload.domain.VerifyUserTag;
import com.oracle.qa.dataload.service.dto.VerifyUserTagDTO;

@Generated(

    value = "org.mapstruct.ap.MappingProcessor",

    date = "2017-07-05T10:11:44+0530",

    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_91 (Oracle Corporation)"

)

@Component

public class VerifyUserTagMapperImpl implements VerifyUserTagMapper {

    @Override

    public VerifyUserTag toEntity(VerifyUserTagDTO dto) {

        if ( dto == null ) {

            return null;
        }

        VerifyUserTag verifyUserTag = new VerifyUserTag();

        verifyUserTag.setId( dto.getId() );

        if ( dto.getInputFile() != null ) {

            byte[] inputFile = dto.getInputFile();

            verifyUserTag.setInputFile( Arrays.copyOf( inputFile, inputFile.length ) );
        }

        verifyUserTag.setInputFileContentType( dto.getInputFileContentType() );

        verifyUserTag.setCategoryId( dto.getCategoryId() );

        verifyUserTag.setStartFrom( dto.getStartFrom() );

        verifyUserTag.setToLine( dto.getToLine() );

        if ( dto.getOutputFile() != null ) {

            byte[] outputFile = dto.getOutputFile();

            verifyUserTag.setOutputFile( Arrays.copyOf( outputFile, outputFile.length ) );
        }

        verifyUserTag.setOutputFileContentType( dto.getOutputFileContentType() );

        verifyUserTag.setVerifyDate( dto.getVerifyDate() );

        return verifyUserTag;
    }

    @Override

    public VerifyUserTagDTO toDto(VerifyUserTag entity) {

        if ( entity == null ) {

            return null;
        }

        VerifyUserTagDTO verifyUserTagDTO = new VerifyUserTagDTO();

        verifyUserTagDTO.setId( entity.getId() );

        if ( entity.getInputFile() != null ) {

            byte[] inputFile = entity.getInputFile();

            verifyUserTagDTO.setInputFile( Arrays.copyOf( inputFile, inputFile.length ) );
        }

        verifyUserTagDTO.setInputFileContentType( entity.getInputFileContentType() );

        verifyUserTagDTO.setCategoryId( entity.getCategoryId() );

        verifyUserTagDTO.setStartFrom( entity.getStartFrom() );

        verifyUserTagDTO.setToLine( entity.getToLine() );

        if ( entity.getOutputFile() != null ) {

            byte[] outputFile = entity.getOutputFile();

            verifyUserTagDTO.setOutputFile( Arrays.copyOf( outputFile, outputFile.length ) );
        }

        verifyUserTagDTO.setOutputFileContentType( entity.getOutputFileContentType() );

        verifyUserTagDTO.setVerifyDate( entity.getVerifyDate() );

        return verifyUserTagDTO;
    }

    @Override

    public List<VerifyUserTag> toEntity(List<VerifyUserTagDTO> dtoList) {

        if ( dtoList == null ) {

            return null;
        }

        List<VerifyUserTag> list = new ArrayList<VerifyUserTag>();

        for ( VerifyUserTagDTO verifyUserTagDTO : dtoList ) {

            list.add( toEntity( verifyUserTagDTO ) );
        }

        return list;
    }

    @Override

    public List<VerifyUserTagDTO> toDto(List<VerifyUserTag> entityList) {

        if ( entityList == null ) {

            return null;
        }

        List<VerifyUserTagDTO> list = new ArrayList<VerifyUserTagDTO>();

        for ( VerifyUserTag verifyUserTag : entityList ) {

            list.add( toDto( verifyUserTag ) );
        }

        return list;
    }
}

