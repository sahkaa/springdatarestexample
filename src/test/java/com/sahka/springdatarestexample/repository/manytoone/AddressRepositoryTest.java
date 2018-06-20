package com.sahka.springdatarestexample.repository.manytoone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahka.springdatarestexample.model.manytoone.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AddressRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postAddress() throws Exception {
        String locationLink = mockMvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(
                        Location.builder().name("locationName").build())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String address = "{\"name\": \"addressName\", \"location\": \"$locationLink\"}".replace("$locationLink",
                locationLink);
        mockMvc.perform(post("/addresses")
                .contentType(MediaType.APPLICATION_JSON).content(address))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get("/addresses/?projection=address"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
