package com.sahka.springdatarestexample.model.projectoin;

import com.sahka.springdatarestexample.model.Organization;
import com.sahka.springdatarestexample.model.Team;
import java.util.List;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "team", types = Team.class)
public interface TeamProjection {

    Long getId();

    String getName();

    List<UserProjection> getUsers();

    Organization getOrganization();
}
