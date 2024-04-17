package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.security.JWTService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.security.SecurityConfiguration;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AuthenticationService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest({AuthenticationController.class, SecurityConfiguration.class})
public class AuthenticationControllerTest {
    @TestConfiguration
    static class MapperTestConfiguration {
        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired private MockMvc mvc;

    @MockBean private UserService userService;

    @MockBean private AuthenticationService authService;

    @MockBean private JWTService jwtService;

    @MockBean private AuthenticationProvider authenticationProvider;

    @Autowired ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {
        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest("testPassword", "testUsername");
        String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
