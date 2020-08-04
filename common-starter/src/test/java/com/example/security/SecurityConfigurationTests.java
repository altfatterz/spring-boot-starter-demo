package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.security.SecurityConfigurationTests.SecurityConfigurationApplication;
import static com.example.security.SecurityConfigurationTests.SecurityConfigurationTestController;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SecurityConfigurationTestController.class, SecurityConfigurationApplication.class})
@AutoConfigureMockMvc
public class SecurityConfigurationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthorized() throws Exception {
        mockMvc.perform(get("/api/greeting")).andExpect(status().isUnauthorized());
    }

    @Test
    public void securedWithToken() throws Exception {
        mockMvc.perform(get("/api/greeting?token=dummy")).andExpect(status().isOk());
    }

    @Test
    public void unsecuredActuatorInfo() throws Exception {
        mockMvc.perform(get("/actuator/info")).andExpect(status().isOk());
    }

    @Test
    public void securedActuatorEnv() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isUnauthorized());
    }

    @Test
    public void accessActuatorEnvWithBasicAuth() throws Exception {
        mockMvc.perform(get("/actuator/env").with(httpBasic("admin", "secret"))).andExpect(status().isOk());
    }

    @RestController
    static class SecurityConfigurationTestController {

        @GetMapping("/api/greeting")
        String greet() {
            return "Hello";
        }
    }

    @SpringBootApplication
    static class SecurityConfigurationApplication {
    }
}

