package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO used when registering a new user
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public record RegisterRequest(
        @NotNull(message = "First name cannot be null")
                @NotBlank(message = "First name cannot be blank")
                String firstName,
        @NotNull(message = "Last name cannot be null")
                @NotBlank(message = "Last name cannot be blank")
                String lastName,
        @NotNull(message = "Username cannot be null")
                @NotBlank(message = "Username cannot be blank")
                String username,
        @NotNull(message = "Password cannot be null")
                @NotBlank(message = "Password cannot be blank")
                String password,
        @NotNull(message = "Email cannot be null") @Email(message = "Invalid email format")
                String email)
        implements Serializable {}
