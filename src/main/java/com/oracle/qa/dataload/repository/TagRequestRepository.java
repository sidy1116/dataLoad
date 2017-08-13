package com.oracle.qa.dataload.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.TagRequest;


/**
 * Spring Data JPA repository for the TagRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRequestRepository extends JpaRepository<TagRequest,Long> {
	
	@Query("SELECT new TagRequest (tag.id,tag.siteId,tag.phints,tag.referelUrl,tag.headers,tag.idType,tag.requestCount,tag.createDate,tag.status)  FROM TagRequest tag ")
    public Page<TagRequest> findAll( Pageable page);
    
}
