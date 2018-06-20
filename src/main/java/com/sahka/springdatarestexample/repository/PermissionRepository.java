package com.sahka.springdatarestexample.repository;

import com.sahka.springdatarestexample.model.Email;
import com.sahka.springdatarestexample.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
