package com.sahka.springdatarestexample.repository.manytoone;

import com.sahka.springdatarestexample.model.manytoone.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LocationRepository extends JpaRepository<Location, Long> {

}
