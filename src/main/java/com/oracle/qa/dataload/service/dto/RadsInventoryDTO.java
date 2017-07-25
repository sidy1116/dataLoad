package com.oracle.qa.dataload.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RadsInventory entity.
 */
public class RadsInventoryDTO implements Serializable {

    private Long id;

    private Integer catId;

    private String catName;

    private LocalDate inventoryDate;

    private Long count;

    private Long prevInvCount;

    private String diffPercentage;
    public RadsInventoryDTO(){
    	
    }
   
    public RadsInventoryDTO(Long id, Integer catId, String catName, LocalDate inventoryDate, Long count,
			Long prevInvCount, String diffPercentage) {
		super();
		this.id = id;
		this.catId = catId;
		this.catName = catName;
		this.inventoryDate = inventoryDate;
		this.count = count;
		this.prevInvCount = prevInvCount;
		this.diffPercentage = diffPercentage;
	}

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

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPrevInvCount() {
        return prevInvCount;
    }

    public void setPrevInvCount(Long prevInvCount) {
        this.prevInvCount = prevInvCount;
    }

    public String getDiffPercentage() {
        return diffPercentage;
    }

    public void setDiffPercentage(String diffPercentage) {
        this.diffPercentage = diffPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RadsInventoryDTO radsInventoryDTO = (RadsInventoryDTO) o;
        if(radsInventoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), radsInventoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RadsInventoryDTO{" +
            "id=" + getId() +
            ", catId='" + getCatId() + "'" +
            ", catName='" + getCatName() + "'" +
            ", inventoryDate='" + getInventoryDate() + "'" +
            ", count='" + getCount() + "'" +
            ", prevInvCount='" + getPrevInvCount() + "'" +
            ", diffPercentage='" + getDiffPercentage() + "'" +
            "}";
    }
}
