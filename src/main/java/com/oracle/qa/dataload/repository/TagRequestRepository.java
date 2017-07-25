package com.oracle.qa.dataload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.TagRequest;


/**
 * Spring Data JPA repository for the TagRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRequestRepository extends JpaRepository<TagRequest,Long> {
    
}
