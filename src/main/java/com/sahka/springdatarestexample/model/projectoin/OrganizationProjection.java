package com.sahka.springdatarestexample.model.projectoin;

import com.sahka.springdatarestexample.model.Organization;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "organization", types = Organization.class)
public interface OrganizationProjection {

    String getName();
}
