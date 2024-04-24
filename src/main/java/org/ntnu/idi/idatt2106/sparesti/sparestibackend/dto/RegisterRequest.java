package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

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
        @NotNull @NotBlank @NotEmpty String firstName,
        @NotNull @NotBlank @NotEmpty String lastName,
        @NotNull @NotBlank @NotEmpty String username,
        @NotNull @NotBlank @NotEmpty String password,
        @NotNull @NotBlank @NotEmpty String email)
        implements Serializable {}
