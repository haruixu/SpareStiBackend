package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 * @param streakStart Streak start date
 * @param streak Streak
 * @param firstDue Date when streak resets
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StreakResponse(
        ZonedDateTime streakStart, @PositiveOrZero Long streak, ZonedDateTime firstDue)
        implements Serializable {}
