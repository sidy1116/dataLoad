package com.oracle.qa.dataload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oracle.qa.dataload.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
