package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.*;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
@RunWith(SpringRunner.class)
class ChangePasswordControllerTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private ChangePasswordRequestService changePasswordRequestService;

    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com");
        String jsonRequest = objectMapper.writeValueAsString(registerRequest);

        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));
    }

    @Test
    void testChangePasswordWithValidEmail() throws Exception {
        ChangePasswordRequestRequest changePasswordRequest =
                new ChangePasswordRequestRequest("testEmail@test.com");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testChangePasswordWithEmailNoUserIsRegisteredWith() throws Exception {
        ChangePasswordRequestRequest changePasswordRequest =
                new ChangePasswordRequestRequest("testEmail2@test.com");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testChangePasswordWithInvalidEmail() throws Exception {
        ChangePasswordRequestRequest changePasswordRequest =
                new ChangePasswordRequestRequest("invalidEmail.com");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePasswordWithInvalidDTO() throws Exception {
        ChangePasswordRequestRequest changePasswordRequest =
                new ChangePasswordRequestRequest("testEmail2@test.com");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);
        String modifiedJson = jsonRequest.replace("\"email\":\"testEmail2@test.com\"", "");

        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePasswordWhenUserAlreadyHasRequestInDB() throws Exception {
        ChangePasswordRequestRequest changePasswordRequest =
                new ChangePasswordRequestRequest("testEmail@test.com");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(
                MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));

        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/changePasswordRequest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPasswordWithValidPassword() throws Exception {
        // ChangePasswordRequest
        UUID uniqueKey = UUID.randomUUID();
        String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());
        changePasswordRequestService.save("testEmail@test.com", encodedUniqueKey);

        // ResetPassword
        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(uniqueKey.toString(), 1L, "Aa12345!");
        String jsonResetPasswordRequest = objectMapper.writeValueAsString(resetPasswordRequest);
        mvc.perform(
                MockMvcRequestBuilders.post("/forgotPassword/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonResetPasswordRequest));

        // Login
        AuthenticationRequest authenticationRequestPreviousPassword =
                new AuthenticationRequest("testUsername", "testPassword123!");
        String jsonRequestLoginPreviousPassword =
                objectMapper.writeValueAsString(authenticationRequestPreviousPassword);

        AuthenticationRequest authenticationRequestNewPassword =
                new AuthenticationRequest("testUsername", "Aa12345!");
        String jsonRequestLoginNewPassword =
                objectMapper.writeValueAsString(authenticationRequestNewPassword);

        // Try to log in with the previous password
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequestLoginPreviousPassword))
                .andExpect(status().isBadRequest());

        // Try to log in with new password
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequestLoginNewPassword))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPasswordWithUserThatDoesNotExist() throws Exception {
        // ChangePasswordRequest
        UUID uniqueKey = UUID.randomUUID();
        String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());
        changePasswordRequestService.save("testEmail@test.com", encodedUniqueKey);

        // ResetPassword
        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(uniqueKey.toString(), 100L, "Aa12345!");
        String jsonResetPasswordRequest = objectMapper.writeValueAsString(resetPasswordRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/resetPassword")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonResetPasswordRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPasswordWithInvalidPassword() throws Exception {
        // ChangePasswordRequest
        UUID uniqueKey = UUID.randomUUID();
        String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());
        changePasswordRequestService.save("testEmail@test.com", encodedUniqueKey);

        // ResetPassword
        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(uniqueKey.toString(), 1L, "Aa12!");
        String jsonResetPasswordRequest = objectMapper.writeValueAsString(resetPasswordRequest);
        mvc.perform(
                        MockMvcRequestBuilders.post("/forgotPassword/resetPassword")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonResetPasswordRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResetPasswordWithInvalidResetID() throws Exception {
        // ChangePasswordRequest
        UUID uniqueKeyRight = UUID.randomUUID();
        UUID uniqueKeyWrong = UUID.randomUUID();
        String encodedUniqueKey = passwordEncoder.encode(uniqueKeyRight.toString());
        changePasswordRequestService.save("testEmail@test.com", encodedUniqueKey);

        // ResetPassword
        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(uniqueKeyWrong.toString(), 1L, "Aa12345!");
        String jsonResetPasswordRequest = objectMapper.writeValueAsString(resetPasswordRequest);
        mvc.perform(
                MockMvcRequestBuilders.post("/forgotPassword/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonResetPasswordRequest));

        // Login
        AuthenticationRequest authenticationRequestNewPassword =
                new AuthenticationRequest("testUsername", "Aa12345!");
        String jsonRequestLoginNewPassword =
                objectMapper.writeValueAsString(authenticationRequestNewPassword);

        // Try to log in with new password
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequestLoginNewPassword))
                .andExpect(status().isBadRequest());
    }
}
