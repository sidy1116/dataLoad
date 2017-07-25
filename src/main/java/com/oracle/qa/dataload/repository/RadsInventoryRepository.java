package com.oracle.qa.dataload.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.RadsInventory;


/**
 * Spring Data JPA repository for the RadsInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RadsInventoryRepository extends JpaRepository<RadsInventory,Long> {
	
	public LocalDate findMaxDate();
	@Query("SELECT i  FROM RadsInventory i where inventoryDate= ?1 ")
	public List<RadsInventory> findRecodsForMaxDate(LocalDate date);
	
	@Query("SELECT i  FROM RadsInventory i where i. inventoryDate BETWEEN ?1 AND ?2 ")
	public List<RadsInventory> findDataForBetweenDate(LocalDate fromDate, LocalDate toDate);
	
	@Query("SELECT i  FROM RadsInventory i where i.catId=?3 AND i. inventoryDate BETWEEN ?1 AND ?2 ")
	public List<RadsInventory> findDataForBetweenDateForCatId(LocalDate fromDate, LocalDate toDate, Integer catId);
    
}
