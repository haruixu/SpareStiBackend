package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.*;

/**
 * DTO used when returning access token
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public record AccessTokenResponse(@NotNull @NotEmpty @NotBlank String accessToken)
        implements Serializable {}
