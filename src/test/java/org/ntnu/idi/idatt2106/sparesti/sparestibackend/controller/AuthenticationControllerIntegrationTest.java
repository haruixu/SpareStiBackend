package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
@RunWith(SpringRunner.class)
class AuthenticationControllerIntegrationTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;

    private String jsonRequest;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
    }

    @Test
    void testRegisterUser() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserWithWeakPassword() throws Exception {
        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "weakPass",
                        "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithUsernameThatAlreadyExists() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegisterUserWithInvalidEmail() throws Exception {
        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "invalidMail");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithMailThatAlreadyExists() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());

        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "testUsername1",
                        "testPassword123!",
                        "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegisterUserWithInvalidFirstName() throws Exception {
        registerRequest =
                new RegisterRequest(
                        "!#¤%&/()",
                        "testLastName", "testUsername", "testPassword123!", "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithInvalidLastName() throws Exception {
        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "!#¤%&/()",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithInvalidUsername() throws Exception {
        registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "a",
                        "testPassword123!",
                        "testEmail@test.com");
        jsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest("testUsername", "testPassword123!");

        String jsonRequest1 = objectMapper.writeValueAsString(registerRequest);
        String jsonRequest2 = objectMapper.writeValueAsString(authenticationRequest);

        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest1));

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest2))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginWithWrongUsername() throws Exception {
        AuthenticationRequest authenticationRequestWrong =
                new AuthenticationRequest("testUsername2", "testPassword123!");
        String jsonRequestWrong = objectMapper.writeValueAsString(authenticationRequestWrong);

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequestWrong))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithWrongPassword() throws Exception {
        AuthenticationRequest authenticationRequestWrong =
                new AuthenticationRequest("testUsername", "testPassword123!2");
        String jsonRequestWrong = objectMapper.writeValueAsString(authenticationRequestWrong);

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequestWrong))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostValidRefreshToken() throws Exception {
        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.post("/auth/register")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .content(jsonRequest))
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String refreshToken = responseJson.get("refreshToken").asText();

        mvc.perform(
                        MockMvcRequestBuilders.get("/auth/renewToken")
                                .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk());
    }

    /*
    @Test
    void testPostInvalidRefreshToken() throws Exception {
        String refreshToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUxMjM0NTYiLCJpYXQiOjE3MTM0NTU2NzIsImV4cCI6MTcxMzQ1NTk3Mn0.ublWYKuMvfbO3P5rUSAJAY_xbKCpnvaUQkcTCMB1n48";

        mvc.perform(
                        MockMvcRequestBuilders.get("/auth/renewToken")
                                .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isForbidden());
    }*/
}
