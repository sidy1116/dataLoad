package com.oracle.qa.dataload.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReTagProfile.
 */
@Entity
@Table(name = "re_tag_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReTagProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "site_id", nullable = false)
    private Integer siteId;

    @NotNull
    @Lob
    @Column(name = "input_file", nullable = false)
    private byte[] inputFile;

    @Column(name = "input_file_content_type", nullable = false)
    private String inputFileContentType;

    @Column(name = "phint")
    private String phint;

    @Column(name = "headers")
    private String headers;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "start_from_line")
    private Integer startFromLine;

    @Column(name = "to_line")
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

    public ReTagProfile siteId(Integer siteId) {
        this.siteId = siteId;
        return this;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public byte[] getInputFile() {
        return inputFile;
    }

    public ReTagProfile inputFile(byte[] inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public void setInputFile(byte[] inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileContentType() {
        return inputFileContentType;
    }

    public ReTagProfile inputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
        return this;
    }

    public void setInputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
    }

    public String getPhint() {
        return phint;
    }

    public ReTagProfile phint(String phint) {
        this.phint = phint;
        return this;
    }

    public void setPhint(String phint) {
        this.phint = phint;
    }

    public String getHeaders() {
        return headers;
    }

    public ReTagProfile headers(String headers) {
        this.headers = headers;
        return this;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public ReTagProfile createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Integer getStartFromLine() {
        return startFromLine;
    }

    public ReTagProfile startFromLine(Integer startFromLine) {
        this.startFromLine = startFromLine;
        return this;
    }

    public void setStartFromLine(Integer startFromLine) {
        this.startFromLine = startFromLine;
    }

    public Integer getToLine() {
        return toLine;
    }

    public ReTagProfile toLine(Integer toLine) {
        this.toLine = toLine;
        return this;
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
        ReTagProfile reTagProfile = (ReTagProfile) o;
        if (reTagProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reTagProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReTagProfile{" +
            "id=" + getId() +
            ", siteId='" + getSiteId() + "'" +
            ", inputFile='" + getInputFile() + "'" +
            ", inputFileContentType='" + inputFileContentType + "'" +
            ", phint='" + getPhint() + "'" +
            ", headers='" + getHeaders() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", startFromLine='" + getStartFromLine() + "'" +
            ", toLine='" + getToLine() + "'" +
            "}";
    }
}
