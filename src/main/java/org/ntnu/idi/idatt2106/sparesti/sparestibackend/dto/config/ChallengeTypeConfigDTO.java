package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeTypeConfig;

/**
 * DTO for {@link ChallengeTypeConfig}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeTypeConfigDTO(
        @NotNull(message = "Challenge type cannot be null") String type,
        @NotNull(message = "General amount cannot be null")
                @Positive(message = "General amount must be positive")
                BigDecimal generalAmount,
        @NotNull(message = "Specific amount cannot be null")
                @Positive(message = "Specific amount must be positive")
                BigDecimal specificAmount)
        implements Serializable {}
