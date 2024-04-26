package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StreakResponse(ZonedDateTime streakStart, @PositiveOrZero Long streak)
        implements Serializable {}
