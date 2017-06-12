package com.oracle.qa.dataload.repository;

import com.oracle.qa.dataload.domain.TagRequest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TagRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRequestRepository extends JpaRepository<TagRequest,Long> {
    
}
