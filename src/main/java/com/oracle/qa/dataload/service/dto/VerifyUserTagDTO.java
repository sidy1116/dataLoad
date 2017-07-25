package com.oracle.qa.dataload.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the VerifyUserTag entity.
 */
public class VerifyUserTagDTO implements Serializable {

    private Long id;

    @NotNull
    @Lob
    private byte[] inputFile;
    private String inputFileContentType;

    private String categoryId;

    private Integer startFrom;

    private Integer toLine;

    @Lob
    private byte[] outputFile;
    private String outputFileContentType;

    private LocalDate verifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getInputFile() {
        return inputFile;
    }

    public void setInputFile(byte[] inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileContentType() {
        return inputFileContentType;
    }

    public void setInputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Integer startFrom) {
        this.startFrom = startFrom;
    }

    public Integer getToLine() {
        return toLine;
    }

    public void setToLine(Integer toLine) {
        this.toLine = toLine;
    }

    public byte[] getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(byte[] outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFileContentType() {
        return outputFileContentType;
    }

    public void setOutputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
    }

    public LocalDate getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(LocalDate verifyDate) {
        this.verifyDate = verifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VerifyUserTagDTO verifyUserTagDTO = (VerifyUserTagDTO) o;
        if(verifyUserTagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), verifyUserTagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VerifyUserTagDTO{" +
            "id=" + getId() +
            ", inputFile='" + getInputFile() + "'" +
            ", categoryId='" + getCategoryId() + "'" +
            ", startFrom='" + getStartFrom() + "'" +
            ", toLine='" + getToLine() + "'" +
            ", outputFile='" + getOutputFile() + "'" +
            ", verifyDate='" + getVerifyDate() + "'" +
            "}";
    }
}
