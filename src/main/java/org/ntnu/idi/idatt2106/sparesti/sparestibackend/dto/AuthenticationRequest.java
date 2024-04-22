package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record AuthenticationRequest(
        @NotNull @NotBlank @NotEmpty String username, @NotNull @NotBlank @NotEmpty String password)
        implements Serializable {}
