package com.oracle.qa.dataload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.ReTagProfile;


/**
 * Spring Data JPA repository for the ReTagProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReTagProfileRepository extends JpaRepository<ReTagProfile,Long> {
    
}
