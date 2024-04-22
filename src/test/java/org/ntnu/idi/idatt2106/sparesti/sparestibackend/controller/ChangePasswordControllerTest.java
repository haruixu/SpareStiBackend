package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChangePasswordRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
@RunWith(SpringRunner.class)
class ChangePasswordControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChangePasswordRequestService changePasswordRequestService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  void testResetPassword() throws Exception {
    //Register
    RegisterRequest registerRequest =
      new RegisterRequest(
        "testFirstName",
        "testLastName",
        "testUsername",
        "testPassword123!",
        "testEmail@test.com");
    String jsonRequestRegister = objectMapper.writeValueAsString(registerRequest);
    mvc.perform(
        MockMvcRequestBuilders.post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .content(jsonRequestRegister));

    //ChangePasswordRequest
    UUID uniqueKey = UUID.randomUUID();
    String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());
    changePasswordRequestService.save("testEmail@test.com", encodedUniqueKey);

    //ResetPassword
    ResetPasswordRequest resetPasswordRequest =
      new ResetPasswordRequest(
        uniqueKey.toString(),
        1L,
        "Aa12345!"
      );
    String jsonResetPasswordRequest = objectMapper.writeValueAsString(resetPasswordRequest);
    mvc.perform(
        MockMvcRequestBuilders.post("/forgotPassword/resetPassword")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .content(jsonResetPasswordRequest));

    //Login
    AuthenticationRequest authenticationRequest =
      new AuthenticationRequest(
        "testUsername",
        "Aa12345!"
      );
    String jsonRequestLogin = objectMapper.writeValueAsString(authenticationRequest);
    mvc.perform(
        MockMvcRequestBuilders.post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .content(jsonRequestLogin))
      .andExpect(status().isOk());
  }
}
