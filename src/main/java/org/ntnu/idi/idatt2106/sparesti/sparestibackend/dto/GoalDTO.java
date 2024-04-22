package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record GoalDTO(
        Long id,
        @NotNull @NotEmpty @NotBlank String title,
        @NotNull @PositiveOrZero BigDecimal saved,
        @NotNull @Positive BigDecimal target,
        @NotNull @PositiveOrZero BigDecimal completion,
        @NotNull @NotEmpty @NotBlank String description,
        @NotNull @PositiveOrZero Long priority,
        @Past ZonedDateTime createdOn,
        ZonedDateTime completedOn,
        @Future ZonedDateTime due)
        implements Serializable {}
