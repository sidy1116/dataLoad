package com.oracle.qa.dataload.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import com.oracle.qa.dataload.domain.enumeration.IdType;

/**
 * A DTO for the TagRequest entity.
 */
public class TagRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer siteId;

    private String phints;

    private String referelUrl;

    private String headers;

    @NotNull
    private IdType idType;

    @NotNull
    private Integer requestCount;

    @Lob
    private byte[] file;
    private String fileContentType;

    private LocalDate createDate;

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

    public String getPhints() {
        return phints;
    }

    public void setPhints(String phints) {
        this.phints = phints;
    }

    public String getReferelUrl() {
        return referelUrl;
    }

    public void setReferelUrl(String referelUrl) {
        this.referelUrl = referelUrl;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagRequestDTO tagRequestDTO = (TagRequestDTO) o;
        if(tagRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tagRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TagRequestDTO{" +
            "id=" + getId() +
            ", siteId='" + getSiteId() + "'" +
            ", phints='" + getPhints() + "'" +
            ", referelUrl='" + getReferelUrl() + "'" +
            ", headers='" + getHeaders() + "'" +
            ", idType='" + getIdType() + "'" +
            ", requestCount='" + getRequestCount() + "'" +
            ", file='" + getFile() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            "}";
    }
}
