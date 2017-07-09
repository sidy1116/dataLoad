package com.oracle.qa.dataload.repository;

import com.oracle.qa.dataload.domain.VerifyUserTag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VerifyUserTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerifyUserTagRepository extends JpaRepository<VerifyUserTag,Long> {
    
}
