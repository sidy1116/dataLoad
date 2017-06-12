package com.oracle.qa.dataload.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.oracle.qa.dataload.domain.enumeration.IdType;

/**
 * A TagRequest.
 */
@Entity
@Table(name = "tag_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "site_id", nullable = false)
    private Integer siteId;

    @Column(name = "phints")
    private String phints;

    @Column(name = "referel_url")
    private String referelUrl;

    @Column(name = "headers")
    private String headers;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", nullable = false)
    private IdType idType;

    @NotNull
    @Column(name = "request_count", nullable = false)
    private Integer requestCount;

    @Lob
    @Column(name = "jhi_file")
    private byte[] file;

    @Column(name = "jhi_file_content_type")
    private String fileContentType;

    @Column(name = "create_date")
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

    public TagRequest siteId(Integer siteId) {
        this.siteId = siteId;
        return this;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getPhints() {
        return phints;
    }

    public TagRequest phints(String phints) {
        this.phints = phints;
        return this;
    }

    public void setPhints(String phints) {
        this.phints = phints;
    }

    public String getReferelUrl() {
        return referelUrl;
    }

    public TagRequest referelUrl(String referelUrl) {
        this.referelUrl = referelUrl;
        return this;
    }

    public void setReferelUrl(String referelUrl) {
        this.referelUrl = referelUrl;
    }

    public String getHeaders() {
        return headers;
    }

    public TagRequest headers(String headers) {
        this.headers = headers;
        return this;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public IdType getIdType() {
        return idType;
    }

    public TagRequest idType(IdType idType) {
        this.idType = idType;
        return this;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public TagRequest requestCount(Integer requestCount) {
        this.requestCount = requestCount;
        return this;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public byte[] getFile() {
        return file;
    }

    public TagRequest file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public TagRequest fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public TagRequest createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
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
        TagRequest tagRequest = (TagRequest) o;
        if (tagRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tagRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TagRequest{" +
            "id=" + getId() +
            ", siteId='" + getSiteId() + "'" +
            ", phints='" + getPhints() + "'" +
            ", referelUrl='" + getReferelUrl() + "'" +
            ", headers='" + getHeaders() + "'" +
            ", idType='" + getIdType() + "'" +
            ", requestCount='" + getRequestCount() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + fileContentType + "'" +
            ", createDate='" + getCreateDate() + "'" +
            "}";
    }
}
