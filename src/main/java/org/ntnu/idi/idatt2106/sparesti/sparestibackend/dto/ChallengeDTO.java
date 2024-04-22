package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeDTO(
        @NotNull Long id,
        @NotNull @NotEmpty @NotBlank String title,
        @NotNull @PositiveOrZero BigDecimal saved,
        @NotNull @PositiveOrZero BigDecimal target,
        @NotNull @PositiveOrZero BigDecimal completion,
        @NotNull @NotEmpty @NotBlank String description,
        @Past ZonedDateTime createdOn,
        ZonedDateTime completedOn,
        @Future ZonedDateTime due,
        @NotNull String type)
        implements Serializable {}
