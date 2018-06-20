package com.sahka.springdatarestexample.repository;

import com.sahka.springdatarestexample.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface EmailRepository extends JpaRepository<Email, Long> {

}
