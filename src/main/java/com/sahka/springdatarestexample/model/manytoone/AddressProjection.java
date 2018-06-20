package com.sahka.springdatarestexample.model.manytoone;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "address", types = Address.class)
public interface AddressProjection {

    String getName();
    Location getLocation();
}
