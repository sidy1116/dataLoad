package com.oracle.qa.dataload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.qa.dataload.domain.VerifyUserTag;


/**
 * Spring Data JPA repository for the VerifyUserTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerifyUserTagRepository extends JpaRepository<VerifyUserTag,Long> {
    
}
