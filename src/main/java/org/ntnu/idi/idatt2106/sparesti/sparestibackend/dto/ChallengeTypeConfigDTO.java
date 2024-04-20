package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Value;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig}
 */
@Value
// @JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeTypeConfigDTO implements Serializable {
    @NotNull ChallengeType type;
    @NotNull @Positive BigDecimal generalAmount;
    @NotNull @Positive BigDecimal specificAmount;
}
