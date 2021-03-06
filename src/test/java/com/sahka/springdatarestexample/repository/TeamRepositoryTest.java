package com.sahka.springdatarestexample.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahka.springdatarestexample.model.Email;
import com.sahka.springdatarestexample.model.Organization;
import com.sahka.springdatarestexample.model.Permission;
import com.sahka.springdatarestexample.model.Team;
import com.sahka.springdatarestexample.model.User;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    private String permissionLink;

    @Before
    public void setUp() throws Exception {
        permissionLink = mockMvc.perform(post("/permissions")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(
                        Permission.builder().name("permissionName").build())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
    }

    @Test
    public void postShouldWorkWithRichJson() throws Exception {
        // create organization that is a root of our model
        String organizationLink = mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(
                        Organization.builder().name("organization").build())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // POST SCENARIO: create team with single user that has single email.
        User user = User.builder().name("user").emails(Collections.singletonList(Email.builder()
                .email("email@email.com").build())).build();
        Team team = Team.builder()
                .users(Collections.singletonList(user)).name("team").build();
        String teamRichJson = objectMapper.writeValueAsString(team);

        //dirty hack(if anybody knows how to inject custom property into string json pls send it to sahkaa@gmail.com):
        // Team is tied to organization above. Use organization link.
        teamRichJson = teamRichJson.replaceFirst("\\{", "{" + "\"organization\": \"" +
                organizationLink + "\", ");
        // dirty hack #2: for user permissions.
        teamRichJson = teamRichJson.replaceFirst(".com\"}]", ".com\"}], " + " \"permission\": \"" +
                permissionLink + "\" ");

        String teamLocation = mockMvc.perform(post("/teams")
                .contentType(MediaType.APPLICATION_JSON).content(teamRichJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(get(teamLocation))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.users[0].emails[0].email").value("email@email.com"))
                .andExpect(jsonPath("$.users[0].name").value("user"))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].emails", hasSize(1)))
                .andExpect(status().isOk());

        // PUT SCENARIO: fully update team's childs
        user = User.builder().name("userAfterPut").emails(Arrays.asList(Email.builder()
                .email("email0@email.com").build(), Email.builder().email("email1@email.com").build())).build();
        team = Team.builder()
                .users(Collections.singletonList(user)).name("team").build();
        teamRichJson = objectMapper.writeValueAsString(team);

        //add organization link
        teamRichJson = teamRichJson.replaceFirst("\\{", "{" + "\"organization\": \"" +
                organizationLink + "\", ");
        // dirty hack #2: for user permissions.
        teamRichJson = teamRichJson.replaceFirst(".com\"}]", ".com\"}], " + " \"permission\": \"" +
                permissionLink + "\" ");

        mockMvc.perform(put(teamLocation)
                .contentType(MediaType.APPLICATION_JSON).content(teamRichJson))
                .andDo(print())
                .andExpect(status().isNoContent());
        mockMvc.perform(get(teamLocation + "/?projection=team"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.users[0].permission.name").value("permissionName"))
                .andExpect(jsonPath("$.users[0].emails[0].email").value("email0@email.com"))
                .andExpect(jsonPath("$.users[0].emails[1].email").value("email1@email.com"))
                .andExpect(jsonPath("$.users[0].name").value("userAfterPut"))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].emails", hasSize(2)))
                .andExpect(status().isOk());

        // check that old users and emails were deleted
        assertThat(userRepository.findAll()).size().isEqualTo(1);
        assertThat(emailRepository.findAll()).size().isEqualTo(2);

        //Patch SCENARIO: update existing user name and his emails, add new user and new email

        mockMvc.perform(patch(teamLocation)
                .contentType(MediaType.APPLICATION_JSON).content(
                        ("{\"users\": [{\"id\": 1, \"permission\": \"permissionLink\",  \"name\": \"nameAfterPatch\", \"emails\": [{\"id\": 1, \"email\": "
                                + "\"email0AfterPath@email.com\"}, {\"id\": 2, \"email\": "
                                + "\"email1AfterPath@email.com\"}]},{\"name\": \"newUserInPatch\", \"permission\": \"permissionLink\",  \"emails\": "
                                + "[{\"email\": \"emailUser2@email.com\"}]}]}\n").replaceAll("permissionLink", permissionLink)))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get(teamLocation + "/?projection=team"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.users[0].permission.name").value("permissionName"))
                .andExpect(jsonPath("$.users[1].permission.name").value("permissionName"))
                .andExpect(jsonPath("$.users[0].emails[0].email").value("email0AfterPath@email.com"))
                .andExpect(jsonPath("$.users[0].emails[1].email").value("email1AfterPath@email.com"))
                .andExpect(jsonPath("$.users[0].name").value("nameAfterPatch"))
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].emails", hasSize(2)))
                .andExpect(jsonPath("$.users[1].emails", hasSize(1)))
                .andExpect(status().isOk());

        // check that old users and emails were deleted
        assertThat(userRepository.findAll()).size().isEqualTo(2);
        assertThat(emailRepository.findAll()).size().isEqualTo(3);
        assertThat(emailRepository.findById(1L).get().getEmail()).isEqualTo("email0AfterPath@email.com");
        assertThat(emailRepository.findById(2L).get().getEmail()).isEqualTo("email1AfterPath@email.com");
        assertThat(emailRepository.findById(3L).get().getEmail()).isEqualTo("emailUser2@email.com");
    }
}
