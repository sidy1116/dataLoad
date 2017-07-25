package com.oracle.qa.dataload.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RadsInventory.
 */
@Entity
@Table(name = "rads_inventory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries(
	    
	    {  
	      @NamedQuery(name = "RadsInventory.findMaxDate",
	          query = "SELECT max( i.inventoryDate ) FROM RadsInventory i"
	          )

	     
	      }  
	    
	    )
public class RadsInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cat_id")
    private Integer catId;

    @Column(name = "cat_name")
    private String catName;

    @Column(name = "inventory_date")
    private LocalDate inventoryDate;

    @Column(name = "count")
    private Long count;

    @Column(name = "prev_inv_count")
    private Long prevInvCount;

    @Column(name = "diff_percentage")
    private String diffPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCatId() {
        return catId;
    }

    public RadsInventory catId(Integer catId) {
        this.catId = catId;
        return this;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public RadsInventory catName(String catName) {
        this.catName = catName;
        return this;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public RadsInventory inventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
        return this;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public Long getCount() {
        return count;
    }

    public RadsInventory count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPrevInvCount() {
        return prevInvCount;
    }

    public RadsInventory prevInvCount(Long prevInvCount) {
        this.prevInvCount = prevInvCount;
        return this;
    }

    public void setPrevInvCount(Long prevInvCount) {
        this.prevInvCount = prevInvCount;
    }

    public String getDiffPercentage() {
        return diffPercentage;
    }

    public RadsInventory diffPercentage(String diffPercentage) {
        this.diffPercentage = diffPercentage;
        return this;
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
        RadsInventory radsInventory = (RadsInventory) o;
        if (radsInventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), radsInventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RadsInventory{" +
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
