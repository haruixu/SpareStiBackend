package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record AuthenticationRequest(
        @NotBlank(message = "Brukernavn eller passord er feil") String username,
        @NotBlank(message = "Brukernavn eller passord er feil") String password)
        implements Serializable {}
