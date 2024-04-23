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
        @NotNull @NotEmpty @NotBlank String title,
        @NotNull @PositiveOrZero BigDecimal saved,
        @NotNull @Positive BigDecimal target,
        @NotNull String description,
        @Future ZonedDateTime due)
        implements Serializable {}
