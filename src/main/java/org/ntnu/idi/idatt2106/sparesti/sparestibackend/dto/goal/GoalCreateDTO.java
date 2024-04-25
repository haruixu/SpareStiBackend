package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record GoalCreateDTO(
        @NotEmpty(message = "Title cannot be empty") @NotBlank(message = "Title cannot be blank")
                String title,
        @NotNull(message = "Saved amount cannot be null")
                @PositiveOrZero(message = "Saved amount cannot be negative")
                BigDecimal saved,
        @NotNull(message = "Target amount cannot be null")
                @Positive(message = "Target amount cannot be less than or equal to zero")
                BigDecimal target,
        @NotNull(message = "Description cannot be null") String description,
        @NotNull(message = "Due date cannot be null")
                @Future(message = "Due date cannot be in the past or now")
                ZonedDateTime due)
        implements Serializable {}
