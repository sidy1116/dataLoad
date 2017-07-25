package com.oracle.qa.dataload.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the ReTagProfile entity.
 */
public class ReTagProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer siteId;

    @NotNull
    @Lob
    private byte[] inputFile;
    private String inputFileContentType;

    private String phint;

    private String headers;

    private LocalDate createDate;

    private Integer startFromLine;

    private Integer toLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
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

    public String getPhint() {
        return phint;
    }

    public void setPhint(String phint) {
        this.phint = phint;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Integer getStartFromLine() {
        return startFromLine;
    }

    public void setStartFromLine(Integer startFromLine) {
        this.startFromLine = startFromLine;
    }

    public Integer getToLine() {
        return toLine;
    }

    public void setToLine(Integer toLine) {
        this.toLine = toLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReTagProfileDTO reTagProfileDTO = (ReTagProfileDTO) o;
        if(reTagProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reTagProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReTagProfileDTO{" +
            "id=" + getId() +
            ", siteId='" + getSiteId() + "'" +
            ", inputFile='" + getInputFile() + "'" +
            ", phint='" + getPhint() + "'" +
            ", headers='" + getHeaders() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", startFromLine='" + getStartFromLine() + "'" +
            ", toLine='" + getToLine() + "'" +
            "}";
    }
}
