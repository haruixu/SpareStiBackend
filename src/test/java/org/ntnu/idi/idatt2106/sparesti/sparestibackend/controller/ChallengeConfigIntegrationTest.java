package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
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
public class ChallengeConfigIntegrationTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

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

        ChallengeTypeConfigDTO challengeTypeConfigDTO =
                new ChallengeTypeConfigDTO("type", BigDecimal.ONE, BigDecimal.ONE);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.VERY_HIGH, Motivation.VERY_HIGH, Set.of(challengeTypeConfigDTO));

        String jsonPostRequest = objectMapper.writeValueAsString(challengeConfigDTO);
        mvc.perform(
                        MockMvcRequestBuilders.post("/config/challenge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetChallengeConfig() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/config/challenge")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.experience").value("VERY_HIGH"))
                .andExpect(jsonPath("$.motivation").value("VERY_HIGH"))
                .andExpect(jsonPath("$.challengeTypeConfigs", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testUpdateChallengeConfig() throws Exception {
        ChallengeTypeConfigDTO challengeTypeConfigDTO =
                new ChallengeTypeConfigDTO("type1", BigDecimal.ONE, BigDecimal.ONE);
        ChallengeTypeConfigDTO challengeTypeConfigDTO1 =
                new ChallengeTypeConfigDTO("type", BigDecimal.ONE, BigDecimal.ONE);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.MEDIUM,
                        Motivation.MEDIUM,
                        Set.of(challengeTypeConfigDTO, challengeTypeConfigDTO1));

        jsonPutRequest = objectMapper.writeValueAsString(challengeConfigDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/config/challenge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPutRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.get("/config/challenge")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.experience").value("MEDIUM"))
                .andExpect(jsonPath("$.motivation").value("MEDIUM"))
                .andExpect(jsonPath("$.challengeTypeConfigs", hasSize(2)));
    }
}
