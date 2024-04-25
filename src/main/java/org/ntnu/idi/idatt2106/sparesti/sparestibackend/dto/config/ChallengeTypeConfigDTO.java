package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeTypeConfigDTO(
        @Size(max = 20, message = "Type can have max 20 characters")
                @NotNull(message = "Challenge type cannot be null")
                String type,
        @NotNull(message = "General amount cannot be null")
                @Positive(message = "General amount must be positive")
                BigDecimal generalAmount,
        @NotNull(message = "Specific amount cannot be null")
                @Positive(message = "Specific amount must be positive")
                BigDecimal specificAmount)
        implements Serializable {}
