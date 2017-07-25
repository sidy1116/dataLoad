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
 * A VerifyUserTag.
 */
@Entity
@Table(name = "verify_user_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VerifyUserTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "input_file", nullable = false)
    private byte[] inputFile;

    @Column(name = "input_file_content_type", nullable = false)
    private String inputFileContentType;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "start_from")
    private Integer startFrom;

    @Column(name = "to_line")
    private Integer toLine;

    @Lob
    @Column(name = "output_file")
    private byte[] outputFile;

    @Column(name = "output_file_content_type")
    private String outputFileContentType;

    @Column(name = "verify_date")
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

    public VerifyUserTag inputFile(byte[] inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public void setInputFile(byte[] inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileContentType() {
        return inputFileContentType;
    }

    public VerifyUserTag inputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
        return this;
    }

    public void setInputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public VerifyUserTag categoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStartFrom() {
        return startFrom;
    }

    public VerifyUserTag startFrom(Integer startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    public void setStartFrom(Integer startFrom) {
        this.startFrom = startFrom;
    }

    public Integer getToLine() {
        return toLine;
    }

    public VerifyUserTag toLine(Integer toLine) {
        this.toLine = toLine;
        return this;
    }

    public void setToLine(Integer toLine) {
        this.toLine = toLine;
    }

    public byte[] getOutputFile() {
        return outputFile;
    }

    public VerifyUserTag outputFile(byte[] outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public void setOutputFile(byte[] outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFileContentType() {
        return outputFileContentType;
    }

    public VerifyUserTag outputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
        return this;
    }

    public void setOutputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
    }

    public LocalDate getVerifyDate() {
        return verifyDate;
    }

    public VerifyUserTag verifyDate(LocalDate verifyDate) {
        this.verifyDate = verifyDate;
        return this;
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
        VerifyUserTag verifyUserTag = (VerifyUserTag) o;
        if (verifyUserTag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), verifyUserTag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VerifyUserTag{" +
            "id=" + getId() +
            ", inputFile='" + getInputFile() + "'" +
            ", inputFileContentType='" + inputFileContentType + "'" +
            ", categoryId='" + getCategoryId() + "'" +
            ", startFrom='" + getStartFrom() + "'" +
            ", toLine='" + getToLine() + "'" +
            ", outputFile='" + getOutputFile() + "'" +
            ", outputFileContentType='" + outputFileContentType + "'" +
            ", verifyDate='" + getVerifyDate() + "'" +
            "}";
    }
}
