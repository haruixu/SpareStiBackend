package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record AuthenticationRequest(
        @NotNull(message = "Username cannot be null")
                @NotBlank(message = "Username cannot be blank")
                @NotEmpty(message = "Username cannot be empty")
                String username,
        @NotNull(message = "Password cannot be null")
                @NotBlank(message = "Password cannot be blank")
                @NotEmpty(message = "Password cannot be empty")
                String password)
        implements Serializable {}
