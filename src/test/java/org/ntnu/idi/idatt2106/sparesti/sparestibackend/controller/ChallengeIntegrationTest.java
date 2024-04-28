package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
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
public class ChallengeIntegrationTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    private String jsonPostRequest;

    private String jsonPutRequest;

    private ChallengeCreateDTO challengeCreateDTO;

    private ChallengeUpdateDTO challengeUpdateDTO;

    @BeforeEach
    void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        challengeCreateDTO =
                new ChallengeCreateDTO(
                        "title",
                        BigDecimal.ZERO,
                        BigDecimal.TEN,
                        BigDecimal.TEN,
                        null,
                        ZonedDateTime.now().plusDays(7),
                        "Type");
        jsonPostRequest = objectMapper.writeValueAsString(challengeCreateDTO);

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

        // Create challenge
        mvc.perform(
                        MockMvcRequestBuilders.post("/challenges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testCreateValidChallenge() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/challenges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGenerateChallengeWithNoActiveConfigTypesGeneratesForAllTypes() throws Exception {
        ChallengeTypeConfigDTO challengeTypeConfigDTO1 =
                new ChallengeTypeConfigDTO("Coffee", BigDecimal.valueOf(100), BigDecimal.TEN);
        ChallengeTypeConfigDTO challengeTypeConfigDTO2 =
                new ChallengeTypeConfigDTO("Snuff", BigDecimal.valueOf(200), BigDecimal.TEN);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.VERY_HIGH,
                        Motivation.VERY_HIGH,
                        Set.of(challengeTypeConfigDTO1, challengeTypeConfigDTO2));

        jsonPostRequest = objectMapper.writeValueAsString(challengeConfigDTO);

        mvc.perform(
                        MockMvcRequestBuilders.post("/config/challenge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.get("/challenges/generate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    void testGenerateChallengeWithOneActiveConfigTypesDoesNotGenerateThisType() throws Exception {
        ChallengeTypeConfigDTO challengeTypeConfigDTO1 =
                new ChallengeTypeConfigDTO("Coffee", BigDecimal.valueOf(100), BigDecimal.TEN);
        ChallengeTypeConfigDTO challengeTypeConfigDTO2 =
                new ChallengeTypeConfigDTO("TyPe", BigDecimal.valueOf(200), BigDecimal.TEN);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.VERY_HIGH,
                        Motivation.VERY_HIGH,
                        Set.of(challengeTypeConfigDTO1, challengeTypeConfigDTO2));

        jsonPostRequest = objectMapper.writeValueAsString(challengeConfigDTO);

        // Post "active" challenge, meaning a challenge of this type will not be generated
        mvc.perform(
                        MockMvcRequestBuilders.post("/config/challenge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonPostRequest))
                .andExpect(status().isOk());

        mvc.perform(
                        MockMvcRequestBuilders.get("/challenges/generate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
