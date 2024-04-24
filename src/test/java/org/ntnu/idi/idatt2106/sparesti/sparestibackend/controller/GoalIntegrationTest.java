package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class GoalIntegrationTest {
    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    private GoalUpdateDTO goalUpdateDTO;

    private String jsonPostRequest;

    private String jsonPutRequest;

    @Autowired private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(1);
        GoalCreateDTO goalCreateDTO =
                new GoalCreateDTO(
                        "title", saved, target, "description", ZonedDateTime.now().plusDays(1));
        jsonPostRequest = objectMapper.writeValueAsString(goalCreateDTO);

        BigDecimal updateSave = new BigDecimal(1);
        BigDecimal updateTarget = new BigDecimal(2);
        goalUpdateDTO =
                new GoalUpdateDTO(
                        "newTitle",
                        updateSave,
                        updateTarget,
                        "newDescription",
                        ZonedDateTime.now().plusDays(1));
        jsonPutRequest = objectMapper.writeValueAsString(goalUpdateDTO);

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
    void testPostUser() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.saved").value(0))
                .andExpect(jsonPath("$.target").value(1))
                .andExpect(jsonPath("$.completion").value(0))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    @WithMockUser
    void testPostUserWithNullBody() throws Exception {
        jsonPostRequest = null;
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    void testGetAllGoals() throws Exception {
        // Create goal
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());

        // Check goal has been added to goals
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testGetSpecificGoal() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.saved").value(0))
                .andExpect(jsonPath("$.target").value(1))
                .andExpect(jsonPath("$.completion").value(0))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    @WithMockUser
    void testGetSpecificGoalNotOwnedByUser() throws Exception {
        // Post goal under mock user
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Register new user
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "user1",
                        "testPassword123!",
                        "testEmail1@test.com");
        String registerJsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(registerJsonRequest));

        // Set new user to currently authenticated user
        UserDetails userDetails = this.userDetailsService.loadUserByUsername("user1");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Attempt to get goal with new user
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testGetActiveGoals() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Verify the newly posted goal is active
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/active")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Test completed size is 0
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @WithMockUser
    void testGetCompleteGoals() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Set completedOn value
        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/goals/1/complete")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedOn", notNullValue()));

        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/active")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    void testSetCompleted() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Verify that no completed goals exist yet
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        // Set completedOn value
        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/goals/1/complete")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedOn", notNullValue()));

        // Verify goal was completed
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testPutGoal() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        jsonPutRequest = objectMapper.writeValueAsString(goalUpdateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("newTitle"))
                .andExpect(jsonPath("$.saved").value(1))
                .andExpect(jsonPath("$.target").value(2))
                .andExpect(jsonPath("$.description").value("newDescription"));
    }

    @Test
    @WithMockUser
    void testPutGoalWithTitleIsNull() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        jsonPutRequest = objectMapper.writeValueAsString(goalUpdateDTO);
        GoalUpdateDTO goalUpdateDTO = new GoalUpdateDTO(null, null, null, null, null);
        jsonPutRequest = objectMapper.writeValueAsString(goalUpdateDTO);
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testPutWithNullValuesDoesNothingToOriginalValues() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        GoalUpdateDTO goalUpdateDTO1 = new GoalUpdateDTO("title", null, null, null, null);
        jsonPutRequest = objectMapper.writeValueAsString(goalUpdateDTO1);

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.saved").value(0))
                .andExpect(jsonPath("$.target").value(1))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    @WithMockUser
    void testDeleteGoal() throws Exception {
        // Post goal
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());

        // Assert size
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Delete goal
        mvc.perform(
                        MockMvcRequestBuilders.delete("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isNoContent());

        // Assert size
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @WithMockUser
    void testDeletingGoalOfOtherUserDoesNothing() throws Exception {
        // Post goal
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());

        // Assert size
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Register new user
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "user1",
                        "testPassword123!",
                        "testEmail1@test.com");
        String registerJsonRequest = objectMapper.writeValueAsString(registerRequest);
        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(registerJsonRequest));

        // Set new user to currently authenticated user
        UserDetails userDetails = this.userDetailsService.loadUserByUsername("user1");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Perform delete under new user
        mvc.perform(
                        MockMvcRequestBuilders.delete("/users/me/goals/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isNoContent());

        // Set authentication context to previous user
        userDetails = this.userDetailsService.loadUserByUsername("user");
        authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Assert size has not changed
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testSetGoalPriority() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.priority").value(2));

        List<Long> goalIds = new ArrayList<>();
        goalIds.add(2L);
        goalIds.add(1L);

        String jsonPutRequest = objectMapper.writeValueAsString(goalIds);
        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/goals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isOk());
        // Verify priority of second goal is bumped up
        mvc.perform(
                        MockMvcRequestBuilders.get("/users/me/goals/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.priority").value(1));
    }
}
