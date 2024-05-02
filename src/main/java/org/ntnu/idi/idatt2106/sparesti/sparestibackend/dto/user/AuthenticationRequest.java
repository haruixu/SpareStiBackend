package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for log in
 * @param username Username
 * @param password password
 */
public record AuthenticationRequest(
        @NotBlank(message = "Brukernavn eller passord er feil") String username,
        @NotBlank(message = "Brukernavn eller passord er feil") String password)
        implements Serializable {}
