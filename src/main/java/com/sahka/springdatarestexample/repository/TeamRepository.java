package com.sahka.springdatarestexample.repository;

import com.sahka.springdatarestexample.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

    @RestResource
    Page<Team> findAllByName(@Param("name") String name, Pageable pageable);
}
