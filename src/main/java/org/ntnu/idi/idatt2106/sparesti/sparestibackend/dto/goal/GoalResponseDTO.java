package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal}
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
public record GoalResponseDTO(
        @NotNull(message = "ID cannot be null") Long id,
        @NotNull(message = "Title cannot be null")
                @NotEmpty(message = "Title cannot be empty")
                @NotBlank(message = "Title cannot be blank")
                String title,
        @NotNull(message = "Saved amount cannot be null")
                @PositiveOrZero(message = "Saved amount cannot be negative")
                BigDecimal saved,
        @NotNull(message = "Target amount cannot be null")
                @Positive(message = "Target amount cannot be less than or equal to zero")
                BigDecimal target,
        @NotNull(message = "Completion amount cannot be null")
                @PositiveOrZero(message = "Completion amount cannot be negative")
                BigDecimal completion,
        @NotNull(message = "Description cannot be null")
                @NotEmpty(message = "Description cannot be empty")
                @NotBlank(message = "Description cannot be blank")
                String description,
        @NotNull(message = "Priority cannot be null")
                @PositiveOrZero(message = "Priority cannot be negative")
                Long priority,
        @Past(message = "Created date must be in the past") ZonedDateTime createdOn,
        @Future(message = "Completed date must be in the future") ZonedDateTime completedOn,
        @Future(message = "Due date must be in the future") ZonedDateTime due)
        implements Serializable {}
