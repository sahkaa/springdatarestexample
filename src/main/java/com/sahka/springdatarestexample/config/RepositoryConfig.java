package com.sahka.springdatarestexample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        //added to use in patch
//        config.exposeIdsFor(User.class, Email.class);
    }
}
