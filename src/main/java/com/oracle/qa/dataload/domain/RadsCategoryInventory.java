package com.oracle.qa.dataload.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RadsCategoryInventory.
 */
@Entity
@Table(name = "rads_category_inventory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RadsCategoryInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cat_id", nullable = false)
    private Integer catId;

    @NotNull
    @Column(name = "cat_name", nullable = false)
    private String catName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCatId() {
        return catId;
    }

    public RadsCategoryInventory catId(Integer catId) {
        this.catId = catId;
        return this;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public RadsCategoryInventory catName(String catName) {
        this.catName = catName;
        return this;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RadsCategoryInventory radsCategoryInventory = (RadsCategoryInventory) o;
        if (radsCategoryInventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), radsCategoryInventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RadsCategoryInventory{" +
            "id=" + getId() +
            ", catId='" + getCatId() + "'" +
            ", catName='" + getCatName() + "'" +
            "}";
    }
}
