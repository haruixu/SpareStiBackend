package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeDTO(
        @NotNull(message = "ID cannot be null") Long id,
        @NotNull(message = "Title cannot be null") @NotBlank(message = "Title cannot be blank")
                String title,
        @NotNull(message = "Saved amount cannot be null")
                @PositiveOrZero(message = "Saved amount cannot be negative")
                BigDecimal saved,
        @NotNull(message = "Target amount cannot be null")
                @PositiveOrZero(message = "Target amount cannot be negative")
                BigDecimal target,
        @NotNull(message = "Per purchase amount cannot be null")
                @Positive(message = "Per purchase amount cannot be less than or equal to zero")
                BigDecimal perPurchase,
        @NotNull(message = "Completion amount cannot be null")
                @PositiveOrZero(message = "Completion amount cannot be negative")
                BigDecimal completion,
        @NotNull(message = "Description cannot be null")
                @NotBlank(message = "Description cannot be blank")
                String description,
        @Past(message = "Created date must be in the past") ZonedDateTime createdOn,
        ZonedDateTime completedOn,
        @Future(message = "Due date must be in the future") ZonedDateTime due,
        @NotNull(message = "Type cannot be null") String type)
        implements Serializable {}
