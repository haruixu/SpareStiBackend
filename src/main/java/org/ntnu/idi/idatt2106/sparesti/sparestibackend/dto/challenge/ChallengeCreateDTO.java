package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeCreateDTO(
        @NotEmpty @NotBlank String title,
        @NotNull @PositiveOrZero BigDecimal saved,
        @NotNull @Positive BigDecimal target,
        @NotNull @Positive BigDecimal perPurchase,
        String description,
        @Future ZonedDateTime due,
        String type)
        implements Serializable {}
