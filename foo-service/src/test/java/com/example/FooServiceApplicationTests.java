package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("swagger")
class FooServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthorized() throws Exception {
        mockMvc.perform(get("/api/foo")).andExpect(status().isUnauthorized());
    }

    @Test
    public void securedWithTokenAsQueryParameter() throws Exception {
        mockMvc.perform(get("/api/foo?token=dummy")).andExpect(status().isOk());
    }

}
