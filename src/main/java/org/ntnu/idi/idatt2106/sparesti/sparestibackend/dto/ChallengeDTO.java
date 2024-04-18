package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
@Value
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeDTO implements Serializable {
    @NotNull @NotEmpty @NotBlank String title;
    @NotNull BigDecimal saved;
    @NotNull @PositiveOrZero BigDecimal target;
    @NotNull @NotEmpty @NotBlank String description;
    @PastOrPresent LocalDateTime createdOn;
    @NotNull ChallengeType type;
}
