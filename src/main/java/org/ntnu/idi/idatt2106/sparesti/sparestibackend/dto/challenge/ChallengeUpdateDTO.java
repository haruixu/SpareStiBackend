package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeUpdateDTO(
        @NotEmpty(message = "Title cannot be empty")
                @NotBlank(message = "Title cannot be blank")
                @Size(max = 20, message = "Title can max have 20 characters")
                String title,
        @NotNull(message = "Saved amount cannot be null")
                @PositiveOrZero(message = "Saved amount cannot be negative")
                BigDecimal saved,
        @NotNull(message = "Target amount cannot be null")
                @Positive(message = "Target amount cannot be less than or equal to zero")
                BigDecimal target,
        @NotNull(message = "Per purchase amount cannot be null")
                @Positive(message = "Per purchase amount cannot be less than or equal to zero")
                BigDecimal perPurchase,
        @Size(max = 280, message = "Description can at most have 280 characters")
                String description,
        @Future(message = "Due date must be in the future") ZonedDateTime due,
        String type)
        implements Serializable {}
