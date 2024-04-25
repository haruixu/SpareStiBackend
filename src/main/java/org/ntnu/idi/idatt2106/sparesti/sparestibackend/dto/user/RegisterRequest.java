package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
                @NotEmpty(message = "First name cannot be empty")
                String firstName,
        @NotNull(message = "Last name cannot be null")
                @NotBlank(message = "Last name cannot be blank")
                @NotEmpty(message = "Last name cannot be empty")
                String lastName,
        @NotNull(message = "Username cannot be null")
                @NotBlank(message = "Username cannot be blank")
                @NotEmpty(message = "Username cannot be empty")
                String username,
        @NotNull(message = "Password cannot be null")
                @NotBlank(message = "Password cannot be blank")
                @NotEmpty(message = "Password cannot be empty")
                String password,
        @NotNull(message = "Email cannot be null") @Email(message = "Invalid email format")
                String email)
        implements Serializable {}
