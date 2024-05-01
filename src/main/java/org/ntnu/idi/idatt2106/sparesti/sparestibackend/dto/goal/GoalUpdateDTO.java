package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record GoalUpdateDTO(
        @NotBlank(message = "Title cannot be blank")
                @Size(max = 20, message = "Title can max be 20 characters")
                String title,
        @PositiveOrZero(message = "Saved amount cannot be negative") BigDecimal saved,
        @Positive(message = "Target amount cannot be less than or equal to zero") BigDecimal target,
        @Size(max = 280, message = "Description can max be 280 characters") String description,
        @Future(message = "Due date cannot be in the past or now") ZonedDateTime due)
        implements Serializable {}
