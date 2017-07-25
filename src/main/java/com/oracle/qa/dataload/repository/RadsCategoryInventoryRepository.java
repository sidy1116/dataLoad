package com.oracle.qa.dataload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.RadsCategoryInventory;


/**
 * Spring Data JPA repository for the RadsCategoryInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RadsCategoryInventoryRepository extends JpaRepository<RadsCategoryInventory,Long> {
    
}
