package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
@Value
@Builder
// @JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeDTO implements Serializable {
    @NotNull Long id;
    @NotNull @NotEmpty @NotBlank String title;
    @NotNull @PositiveOrZero BigDecimal saved;
    @NotNull @PositiveOrZero BigDecimal target;
    @NotNull @PositiveOrZero BigDecimal completion;
    @NotNull @NotEmpty @NotBlank String description;
    @Past ZonedDateTime createdOn;
    ZonedDateTime completedOn;
    @Future ZonedDateTime due;
    @NotNull String type;
}
