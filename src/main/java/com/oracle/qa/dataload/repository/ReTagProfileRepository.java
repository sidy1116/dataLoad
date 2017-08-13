package com.oracle.qa.dataload.repository;

import com.oracle.qa.dataload.domain.ReTagProfile;
import com.oracle.qa.dataload.domain.TagRequest;
import com.oracle.qa.dataload.domain.enumeration.Status;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReTagProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReTagProfileRepository extends JpaRepository<ReTagProfile,Long> {
	
	
	@Query("SELECT new ReTagProfile ( retag.id,  retag.siteId,  retag.phint,  retag.headers,  retag.createDate,retag.startFromLine,  retag.toLine,  retag.reTagCount,  retag.status)  FROM ReTagProfile retag")
    public Page<ReTagProfile> findAll( Pageable page);
    
}
