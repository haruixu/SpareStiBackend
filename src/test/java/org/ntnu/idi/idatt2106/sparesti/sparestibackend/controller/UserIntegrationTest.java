package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserIntegrationTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    private UserUpdateDTO updateDTO;

    private String jsonPutRequest;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // Prepare user
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "user",
                        "testPassword123!",
                        "testEmail@test.com");
        String registerJsonRequest = objectMapper.writeValueAsString(registerRequest);

        // Post user
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(registerJsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void test() {}

    @Test
    @WithMockUser
    void testUpdatingUserWithValidData() throws Exception {
        updateDTO =
                new UserUpdateDTO(
                        "firstName", "lastName", "Aa12345!", "username", "xu@xu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.email").value("xu@xu.com"));
    }

    @Test
    @WithMockUser
    void testUpdatingUserWithInvalidData() throws Exception {
        // Bad last name
        updateDTO =
                new UserUpdateDTO("firstName", "", "Aa12345!", "username", "xu@xu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isBadRequest());

        // Bad first name
        updateDTO = new UserUpdateDTO("", "aaa", "Aa12345!", "username", "xu@xu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isBadRequest());

        // Weak password
        updateDTO = new UserUpdateDTO("aa", "", "Aa12345", "username", "xu@xu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isBadRequest());

        // Invalid email
        updateDTO = new UserUpdateDTO("aa", "aa", "Aa12345!", "username", "xuxu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        // Invalid email
        updateDTO = new UserUpdateDTO("aa", "aa", "Aa12345!", "username", "xu.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);

        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "user1",
                        "testPassword123!",
                        "test@test.com");
        String registerJsonRequest = objectMapper.writeValueAsString(registerRequest);

        // Post user
        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(registerJsonRequest))
                .andExpect(status().isOk());

        // Duplicate email
        updateDTO =
                new UserUpdateDTO("aa", "aa", "Aa12345!", "username", "test@test.com", null, null);
        jsonPutRequest = objectMapper.writeValueAsString(updateDTO);
        mvc.perform(
                        MockMvcRequestBuilders.put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isConflict());
    }
}
