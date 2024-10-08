package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO used returning JWT tokens upon successful login and register
 * @param accessToken Access JWT token
 * @param refreshToken Refersh JWT token
 * @author Harry L.X and Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public record LoginRegisterResponse(
        @NotNull @NotBlank @NotEmpty String accessToken,
        @NotNull @NotBlank @NotEmpty String refreshToken)
        implements Serializable {}
