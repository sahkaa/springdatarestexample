package com.sahka.springdatarestexample.model.projectoin;

import com.sahka.springdatarestexample.model.Email;
import com.sahka.springdatarestexample.model.Permission;
import com.sahka.springdatarestexample.model.User;
import java.util.List;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "user", types = User.class)
public interface UserProjection {

    Long getId();

    List<Email> getEmails();

    Permission getPermission();

    String getName();
}
