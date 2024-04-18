package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
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
class AuthenticationControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  void testRegisterUser() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isOk());
  }

  @Test
  void testRegisterUserWithWeakPassword() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "pass");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isBadRequest());
  }

  @Test
  void testRegisterUserWithUsernameThatAlreadyExists() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest));

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isConflict());
  }

  @Test
  void testLoginWithValidCredentials() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest));

    mvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isOk());
  }

  @Test
  void testLoginWithWrongUsername() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    AuthenticationRequest authenticationRequestWrong = new AuthenticationRequest("testUsername2", "testPassword123!");
    String jsonRequestWrong = objectMapper.writeValueAsString(authenticationRequestWrong);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(jsonRequest));

    mvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequestWrong))
      .andExpect(status().isBadRequest());
  }

  @Test
  void testLoginWithWrongPassword() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    AuthenticationRequest authenticationRequestWrong = new AuthenticationRequest("testUsername", "testPassword123!2");
    String jsonRequestWrong = objectMapper.writeValueAsString(authenticationRequestWrong);

    mvc.perform(MockMvcRequestBuilders.post("/auth/register")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(jsonRequest));

    mvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequestWrong))
      .andExpect(status().isBadRequest());
  }

  @Test
  void testPostValidRefreshToken() throws Exception {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUsername", "testPassword123!");
    String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String refreshToken = responseJson.get("refreshToken").asText();

    mvc.perform(MockMvcRequestBuilders.get("/auth/renewToken")
        .header("Authorization", "Bearer " + refreshToken))
      .andExpect(status().isOk());
  }

  //TODO: Fix testPostInvalidRefreshToken test. Also make reusable methods for repeating code in tests.

  /*
  @Test
  void testPostInvalidRefreshToken() throws Exception {
    String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUxMjM0NTYiLCJpYXQiOjE3MTM0NTU2NzIsImV4cCI6MTcxMzQ1NTk3Mn0.ublWYKuMvfbO3P5rUSAJAY_xbKCpnvaUQkcTCMB1n48";

    mvc.perform(MockMvcRequestBuilders.get("/auth/renewToken")
        .header("Authorization", "Bearer " + refreshToken))
      .andExpect(status().isForbidden());
  }
   */
}
