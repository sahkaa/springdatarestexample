package com.sahka.springdatarestexample.repository;

import com.sahka.springdatarestexample.model.Organization;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Page<Organization> findAllByIdNotNull(Pageable pageable);

    List<Organization> findAllByLabelNotNull();
}
