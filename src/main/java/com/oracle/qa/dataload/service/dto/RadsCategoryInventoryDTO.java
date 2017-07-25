package com.oracle.qa.dataload.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the RadsCategoryInventory entity.
 */
public class RadsCategoryInventoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer catId;

    @NotNull
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

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
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

        RadsCategoryInventoryDTO radsCategoryInventoryDTO = (RadsCategoryInventoryDTO) o;
        if(radsCategoryInventoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), radsCategoryInventoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RadsCategoryInventoryDTO{" +
            "id=" + getId() +
            ", catId='" + getCatId() + "'" +
            ", catName='" + getCatName() + "'" +
            "}";
    }
}
