package com.oracle.qa.dataload.repository;

import com.oracle.qa.dataload.domain.ReTagProfile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReTagProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReTagProfileRepository extends JpaRepository<ReTagProfile,Long> {
    
}
