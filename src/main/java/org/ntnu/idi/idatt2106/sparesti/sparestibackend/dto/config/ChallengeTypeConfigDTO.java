package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeTypeConfigDTO(
        @NotNull String type,
        @NotNull @Positive BigDecimal generalAmount,
        @NotNull @Positive BigDecimal specificAmount)
        implements Serializable {}
